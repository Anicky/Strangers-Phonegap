<?php

/**
 * Paramètres :
 * params : Tableau JSON des comptes emails. Chaque compte a les paramètres user, pass, serv, ssl, port, mail, boxes.
 * num : Le numéro de téléphone à chercher
 * (optionnel) nb : Nombre de mails à récupérer (du + récent au + ancien)
 * Réponse :
 * status : 'error' si une erreur s'est produite, 'not found' si le numéro n'a pas été trouvé, 'ok' si un numéro a été trouvé
 * firstname : le prénom de la personne qui envoie (on se base sur l'entête du mail, le "FROM")
 * lastname : le nom de la personne qui envoie (on se base sur l'entête du mail, le "FROM")
 */
$reponse = '{"status":"error"}';
if ((isset($_POST['params'])) && (isset($_POST['num']))) {
    if (($_POST['params'] != "") && ($_POST['num'] != "")) {
        require_once("fonctions.php");
        $reponse = '{"status":"not found"}';
        $trouve = false;
        $prenom = "";
        $nom = "";
        $params = json_decode($_POST['params']);
        $limite_emails = 0;
        if (isset($_POST['nb'])) {
            $limite_emails = htmlspecialchars($_POST['nb']);
            if ($limite_emails < 0) {
                $limite_emails = 0;
            }
        }
        $nombre_comptes = count($params);
        $compteur_comptes = 0;
        // Création de l'expression régulière
        $numero = preg_replace('/[^0-9]/', '', htmlspecialchars($_POST['num'])); // Mise en forme du numéro de téléphone à chercher : on enlève les espaces, les tirets, les points...pour ne garder qu'une suite de chiffres
        $numero_parts = str_split($numero, 2); // Séparation de la chaine de caractères en groupes de 2 numéros
        $delimiter = "[-./\\ ]?";
        $regexp = "#(";
        $numero_parts_length = count($numero_parts);
        foreach ($numero_parts as $i => $part) {
            $regexp .= $part;
            if ($i <= $numero_parts_length - 2) {
                $regexp .= $delimiter;
            }
        }
        $regexp .= ")#";
        // Fin de l'expression régulière
        while ((!$trouve) && ($compteur_comptes < $nombre_comptes)) {
            $serveur = $params[$compteur_comptes]->serv;
            $port = $params[$compteur_comptes]->port;
            $username = $params[$compteur_comptes]->user;
            $password = $params[$compteur_comptes]->pass;
            $ssl = "";
            if ($params[$compteur_comptes]->ssl == "1") {
                $ssl = "/ssl";
            }
            $hostname = get_hostname($serveur, $port, $ssl);
            $nombre_boites = count($params[$compteur_comptes]->boxes);
            $compteur_boites = 0;
            while ((!$trouve) && ($compteur_boites < $nombre_boites)) {
                $inbox = imap_open($hostname . $params[$compteur_comptes]->boxes[$compteur_boites], $username, $password) or die('Problème de connexion : ' . imap_last_error());
                if ($inbox) {
                    $emails = imap_search($inbox, 'ALL');
                    if ($emails) {
                        rsort($emails); // Inverse l'ordre pour afficher les emails les plus récents en premier
                        $nombre_emails = $limite_emails;
                        if (($nombre_emails == 0) || ($nombre_emails > count($emails))) {
                            $nombre_emails = count($emails);
                        }
                        $compteur_emails = 0;
                        while ((!$trouve) && ($compteur_emails < $nombre_emails)) {
                            $numero_email = $emails[$compteur_emails];
                            $apercu = imap_fetch_overview($inbox, $numero_email, 0);
                            $message = imap_fetchbody($inbox, $numero_email, 1, FT_PEEK);
                            if (preg_match($regexp, $message)) {
                                $expediteur_array = explode("<", decode_imap_from($apercu[0]->from));
                                $expediteur = explode(" ", $expediteur_array[0]);
                                $prenom = $expediteur[0];
                                if (count($expediteur) > 1) {
                                    $nom = $expediteur[1];
                                }
                                $trouve = true;
                            }
                            $compteur_emails++;
                        }
                    }
                    imap_close($inbox);
                }
                $compteur_boites++;
            }
            $compteur_comptes++;
        }
        if ($trouve) {
            $reponse = '{"status":"ok","firstname":"' . $prenom . '","lastname":"' . $nom . '"}';
        }
    }
}
echo $reponse;
?>