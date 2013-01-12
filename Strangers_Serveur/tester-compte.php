<?php

/**
 * Paramètres (obligatoires) :
 * serv : L'adresse du serveur
 * port : Le port
 * user : Le nom d'utilisateur
 * pass : Le mot de passe
 * Paramètres (facultatifs) :
 * ssl : Sécurité de la connexion : aucune ou SSL/TLS (par défaut : aucune)
 * Réponse :
 * status : 'error' si une erreur s'est produite, 'ok' si la fonction s'est bien déroulée
 * boxes : un tableau avec la liste des noms des boîtes mail
 */
$reponse = '{"status":"error"}';
if ((isset($_POST['serv'])) && (isset($_POST['port'])) && (isset($_POST['user'])) && (isset($_POST['pass']))) {
    if (($_POST['serv'] != "") && ($_POST['port'] != "") && ($_POST['user'] != "") && ($_POST['pass'] != "")) {
        $serveur = htmlspecialchars($_POST['serv']);
        $port = htmlspecialchars($_POST['port']);
        $username = htmlspecialchars($_POST['user']);
        $password = htmlspecialchars($_POST['pass']);
        $ssl = "";
        if (isset($_POST['ssl'])) {
            $ssl = "/ssl";
        }
        require_once("fonctions.php");
        $hostname = get_hostname($serveur, $port, $ssl);
        $liste_boites = get_boites($hostname, $username, $password);
        if ($liste_boites) {
            $reponse = '{"status":"ok","boxes":' . json_encode($liste_boites) . '}';
        }
    }
}
echo $reponse;
?>
