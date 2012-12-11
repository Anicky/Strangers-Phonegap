<?php
/**
 * Param�tres (obligatoires) :
 * serv : L'adresse du serveur
 * port : Le port
 * user : Le nom d'utilisateur
 * pass : Le mot de passe
 * Param�tres (facultatifs) :
 * num : Le num�ro de t�l�phone � chercher
 * folder : Le dossier o� chercher les mails (par d�faut : INBOX (boite de r�ception))
 * ssl : S�curit� de la connexion : aucune ou SSL/TLS (par d�faut : aucune)
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

        $hostname = '{' . $serveur . ':' . $port . '/imap' . $ssl . '}';

        set_time_limit(60);
        
        $inbox = imap_open($hostname . $folder, $username, $password) or die('Probl�me de connexion : ' . imap_last_error());

        require_once("fonctions.php");
        ?>
        <article>
            <?php
            recuperation_dossiers($inbox, $hostname);
            ?>
        </article>
        <?php
        if ($inbox) {
            $emails = imap_search($inbox, 'ALL');

            if ($emails) {
                /* Inverse l'ordre pour afficher les emails les plus r�cents en premier */
                rsort($emails);
                $sortie = '';
                foreach ($emails as $nombre_emails) {

                    /* Informations sur l'email */
                    $apercu = imap_fetch_overview($inbox, $nombre_emails, 0);
                    $message = imap_fetchbody($inbox, $nombre_emails, 2);

                    /* Affichage de l'entete de l'email */
                    $sortie.= '<article>';
                    $sortie.= '<div class="header-' . ($apercu[0]->seen ? 'lu' : 'non-lu') . '">';
                    $sortie.= '<span class="sujet">Sujet : <strong>' . $apercu[0]->subject . '</strong></span> ';
                    $sortie.= '<span class="expediteur">Exp�diteur : ' . $apercu[0]->from . '</span>';
                    $sortie.= '<span class="date">Date : <em>' . $apercu[0]->date . '</em></span>';
                    $sortie.= '</div>';

                    /* Affichage du corps de l'email */
                    $sortie.= '<div class="body">' . $message . '</div>';
                    $sortie.= '</article>';
                }

                echo $sortie;
            }
        }
        imap_close($inbox);
    }
}
?>