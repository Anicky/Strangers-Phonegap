<?php

/**
 * Paramètres (obligatoires) :
 * serv : L'adresse du serveur
 * port : Le port
 * user : Le nom d'utilisateur
 * pass : Le mot de passe
 */
if ((isset($_POST['serv'])) && (isset($_POST['port'])) && (isset($_POST['user'])) && (isset($_POST['pass']))) {
    if (($_POST['serv'] != "") && ($_POST['port'] != "") && ($_POST['user'] != "") && ($_POST['pass'] != "")) {
        $serveur = $_POST['serv'];
        $port = $_POST['port'];
        $username = $_POST['user'];
        $password = $_POST['pass'];

        $hostname = '{' . $serveur . ':' . $port . '/imap' . $ssl . '}';

        $inbox = imap_open($hostname, $username, $password) or die('Problème de connexion : ' . imap_last_error());
        if ($inbox) {
            require_once("fonctions.php");
            recuperation_dossiers($inbox, $hostname);
        } else {
            echo "erreur";
        }
    }
}
?>
