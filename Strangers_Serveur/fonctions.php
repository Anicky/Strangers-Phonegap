<?php

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
