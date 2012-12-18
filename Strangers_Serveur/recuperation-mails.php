<?php
/**
 * Paramètres (obligatoires) :
 * serv : L'adresse du serveur
 * port : Le port
 * user : Le nom d'utilisateur
 * pass : Le mot de passe
 * Paramètres (facultatifs) :
 * num : Le numéro de téléphone à chercher
 * folders : Les dossier où chercher les mails (par défaut : INBOX (boite de réception))
 * ssl : Sécurité de la connexion : aucune ou SSL/TLS (par défaut : aucune)
 * nb : Nombre de mails à récupérer (du + récent au + ancien)
 * 
 */
if ((isset($_POST['serv'])) && (isset($_POST['port'])) && (isset($_POST['user'])) && (isset($_POST['pass'])) && (isset($_POST['num']))) {
    if (($_POST['serv'] != "") && ($_POST['port'] != "") && ($_POST['user'] != "") && ($_POST['pass'] != "") && ($_POST['num'] != "")) {

        $serveur = htmlspecialchars($_POST['serv']);
        $port = htmlspecialchars($_POST['port']);
        $username = htmlspecialchars($_POST['user']);
        $password = htmlspecialchars($_POST['pass']);

        $folders = array("INBOX");
        if (isset($_POST['folders'])) {
            $folders = $_POST['folders'];
        }

        $numero = "";
        if (isset($_POST['num'])) {
            $numero = htmlspecialchars($_POST['num']);
        }

        $nombre_emails = 0;
        if (isset($_POST['nb'])) {
            $nombre_emails = htmlspecialchars($_POST['nb']);
        }
        if ($nombre_emails < 0) {
            $nombre_emails = 0;
        }

        $ssl = "";
        if (isset($_POST['ssl'])) {
            $ssl = "/ssl";
        }

        require_once("fonctions.php");
        $hostname = get_hostname($serveur, $port, $ssl);

        $inbox = imap_open($hostname . "INBOX", $username, $password) or die('Problème de connexion : ' . imap_last_error());

        if ($inbox) {
            $emails = imap_search($inbox, 'ALL');

            if ($emails) {
                /* Inverse l'ordre pour afficher les emails les plus récents en premier */
                rsort($emails);
                $sortie = '';

                if ($nombre_emails == 0) {
                    $nombre_emails = count($emails);
                }

                for ($i = 0; $i < $nombre_emails; $i++) {

                    $numero_email = $emails[$i];

                    /* Informations sur l'email */
                    $apercu = imap_fetch_overview($inbox, $numero_email, 0);
                    $message = imap_fetchbody($inbox, $numero_email, 1, FT_PEEK);

                    /* Affichage de l'entete de l'email */
                    $sortie.= '<article>';
                    $sortie.= '<div class="header-' . ($apercu[0]->seen ? 'lu' : 'non-lu') . '">';
                    $sortie.= '<span class="sujet">Sujet : <strong>' . $apercu[0]->subject . '</strong></span> ';
                    $sortie.= '<span class="expediteur">Expéditeur : ' . $apercu[0]->from . '</span>';
                    $sortie.= '<span class="date">Date : <em>' . $apercu[0]->date . '</em></span>';
                    $sortie.= '</div>';

                    /* Affichage du corps de l'email */
                    $sortie.= '<div class="body">' . $message . '</div>';

                    $sortie.= '</article>';
                }

                // Séparation de la chaine de caractères en groupes de 2 numéros
                $numero_parts = str_split($numero, 2);

                // Création de l'expression régulière
                $delimiter = "[-./\\ ]?";
                $regexp = "#(";
                foreach ($numero_parts as $part) {
                    $regexp .= $part . $delimiter;
                }
                $regexp .= ")#";

                // Chaine de remplacement (là on leur applique un style CSS pour les repérer facilement sur la page !)
                $remplacement = '<span class="telephoneTrouve">${1}</span>';

                // Remplacement des numéros de téléphone
                $sortie = preg_replace($regexp, $remplacement, $sortie);

                // Debug
                ?>
                <div class="debug">
                    <h1>Debug</h1><br />
                    <?php
                    echo "Numéro : " . $numero . " (" . strlen($numero) . " caracteres)<br />";
                    var_dump($numero_parts);
                    echo "Expression régulière : " . $regexp;
                    ?>
                </div>
                <?php
                // Affichage des mails avec numéro de téléphone trouvé affiché en gras et rouge
                echo $sortie;
            }
        }
        imap_close($inbox);
    }
}
?>