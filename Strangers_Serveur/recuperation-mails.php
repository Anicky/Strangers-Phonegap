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
if ((isset($_POST['serv'])) && (isset($_POST['port'])) && (isset($_POST['user'])) && (isset($_POST['pass'])) && (isset($_POST['num'])) ) {
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
                $message_global = '';
                
                if ($nombre_emails == 0) {
                     $nombre_emails = count($emails);
                }

                for ($i = 0; $i < $nombre_emails; $i++) {

                    $numero_email = $emails[$i];

                    /* Informations sur l'email */
                    $apercu = imap_fetch_overview($inbox, $numero_email, 0);
                    $message= imap_fetchbody($inbox, $numero_email, 1, FT_PEEK);
                    $message_global .= $message;                  

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

               //echo $sortie;
               //echo $message_global;
            
                echo "<br/>";
                $tableau[]='';
                $numero_a_verifier = $_POST['num'];
                $longeur_chaine_a_verifier =strlen($_POST['num']);
                $nombre_num_valide = preg_match_all("#0[1-9]([-./\\ ]?[0-9]{2}){4}#",$message_global, $tableau,PREG_PATTERN_ORDER);
                echo $nombre_num_valide; echo "<br/>";
                var_dump($tableau[0]);
                //$longeur_chaine_extraite=$tableau[0][0];
                echo $numero_a_verifier;echo "<br/>";
                echo $longeur_chaine_a_verifier;echo "<br/>";
               // echo $longeur_chaine_extraite;echo "<br/>";
                
                for($k=0;$k<$nombre_num_valide;$k++){
                   // $liste_numero=$tableau[0][$k];
                    $liste_numero=$tableau[0][0];
                for($j=0,$i=0;$j<14,$i<$longeur_chaine_a_verifier;$j=$j+3,$i=$i+2){
                   
                    
                    $chaine1 = substr($numero_a_verifier, $i, $i+2);
                    $chaine2 = substr($liste_numero, $j,$j+2);
                    
                    
                    if( strcmp($chaine1,$chaine2) == 0 ){
                        echo "OK";
                    } else echo "KO";
                    
                    echo "<br/>";              
                    echo $chaine1; echo "<br/>";
                    echo $chaine2; echo "<br/>";
                    echo strcmp($chaine1,$chaine2);echo "<br/>";
                    
                    //$array_num = preg_grep("#0[1-9]([-./\\ ]?[0-9]{2}){4}#",);
                    
                    }}
            }
        }
        imap_close($inbox);
    }
    
}
?>