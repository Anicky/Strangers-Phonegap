<?php

if ((isset($_POST['num'])) && (isset($_POST['serv'])) && (isset($_POST['port'])) && (isset($_POST['user'])) && (isset($_POST['pass']))) {
    if (($_POST['num'] != "") && ($_POST['serv'] != "") && ($_POST['port'] != "") && ($_POST['user'] != "") && ($_POST['pass'] != "")) {

        $numero = $_POST['num'];
        $serveur = $_POST['serv'];
        $port = $_POST['port'];
        $username = $_POST['user'];
        $password = $_POST['pass'];
        $folder = "INBOX";
        if (isset($_POST['folder'])) {
            $folder = $_POST['folder'];
        }

        /* Récupération des mails */
        $hostname = '{' . $serveur . ':' . $port . '/imap/ssl}' . $folder;

        $inbox = imap_open($hostname, $username, $password) or die('Problème de connexion : ' . imap_last_error());

        if ($inbox) {
            $emails = imap_search($inbox, 'ALL');

            /* if emails are returned, cycle through each... */
            if ($emails) {

                /* begin output var */
                $output = '';

                /* put the newest emails on top */
                rsort($emails);

                /* for every email... */
                foreach ($emails as $email_number) {

                    /* get information specific to this email */
                    $overview = imap_fetch_overview($inbox, $email_number, 0);
                    $message = imap_fetchbody($inbox, $email_number, 2);

                    /* output the email header information */
                    $output.= '<div class="toggler ' . ($overview[0]->seen ? 'read' : 'unread') . '">';
                    $output.= '<span class="subject">' . $overview[0]->subject . '</span> ';
                    $output.= '<span class="from">' . $overview[0]->from . '</span>';
                    $output.= '<span class="date">on ' . $overview[0]->date . '</span>';
                    $output.= '</div>';

                    /* output the email body */
                    $output.= '<div class="body">' . $message . '</div>';
                }

                echo $output;
            }
        }
        /* close the connection */
        imap_close($inbox);
    }
}
?>