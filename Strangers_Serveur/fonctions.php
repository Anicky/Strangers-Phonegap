<?php

/**
 * Créé une adresse pour accéder à un compte email
 * @param type $server L'adresse du serveur
 * @param type $port Le numéro de port
 * @param type $ssl Indique si la connexion est en SSL
 * @param type $folder Le dossier à ouvrir
 * @return type
 */
function get_hostname($server, $port, $ssl = "", $folder = "") {
    return '{' . $server . ':' . $port . '/imap' . $ssl . '}' . $folder;
}

function get_dossiers($hostname, $username, $password) {
    $retour = false;
    $inbox = imap_open($hostname, $username, $password);
    if ($inbox) {
        $liste_dossiers = imap_getmailboxes($inbox, $hostname, "*");
        if (is_array($liste_dossiers)) {
            $retour = $liste_dossiers;
        }
    }
    return $retour;
}

?>
