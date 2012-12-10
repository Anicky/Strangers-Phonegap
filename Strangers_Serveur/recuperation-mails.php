<?php

/**
 * Paramètres (obligatoires) :
 * serv : L'adresse du serveur
 * port : Le port
 * user : Le nom d'utilisateur
 * pass : Le mot de passe
 * Paramètres (facultatifs) :
 * num : Le numéro de téléphone à chercher
 * folder : Le dossier où chercher les mails (par défaut : INBOX (boite de réception))
 * ssl : Sécurité de la connexion : aucune ou SSL/TLS (par défaut : aucune)
 * 
 */
if ((isset($_POST['serv'])) && (isset($_POST['port'])) && (isset($_POST['user'])) && (isset($_POST['pass']))) {
    if (($_POST['serv'] != "") && ($_POST['port'] != "") && ($_POST['user'] != "") && ($_POST['pass'] != "")) {

        $serveur = $_POST['serv'];
        $port = $_POST['port'];
        $username = $_POST['user'];
        $password = $_POST['pass'];

        $folder = "INBOX";
        if (isset($_POST['folder'])) {
            $folder = $_POST['folder'];
        }

        $numero = "";
        if (isset($_POST['num'])) {
            $numero = $_POST['num'];
        }

        $ssl = "";
        if (isset($_POST['ssl'])) {
            $ssl = "/ssl";
        }

        /* Récupération des mails */
        $hostname = '{' . $serveur . ':' . $port . '/imap' . $ssl . '}' . $folder;

        $inbox = imap_open($hostname, $username, $password) or die('Problème de connexion : ' . imap_last_error());

        if ($inbox) {
            $emails = imap_search($inbox, 'ALL');

            /* if emails are returned, cycle through each... */
            if ($emails) {

                /* begin output var */
                $sortie = '';

                /* put the newest emails on top */
                rsort($emails);

                /* for every email... */
                foreach ($emails as $nombre_emails) {

                    /* get information specific to this email */
                    $apercu = imap_fetch_overview($inbox, $nombre_emails, 0);
                    $message = imap_fetchbody($inbox, $nombre_emails, 2);

                    /* output the email header information */
                    $sortie.= '<article>';
                    $sortie.= '<div class="toggler ' . ($apercu[0]->seen ? 'read' : 'unread') . '">';
                    $sortie.= '<span class="subject">' . $apercu[0]->subject . '</span> ';
                    $sortie.= '<span class="from">' . $apercu[0]->from . '</span>';
                    $sortie.= '<span class="date">on ' . $apercu[0]->date . '</span>';
                    $sortie.= '</div>';

                    /* output the email body */
                    $sortie.= '<div class="body">' . $message . '</div>';
                    $sortie.= '</article>';
                }

                echo $sortie;
            }
        }
        /* close the connection */
        imap_close($inbox);
    }
}
?>