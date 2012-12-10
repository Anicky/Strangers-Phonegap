<?php

function recuperation_dossiers($inbox, $hostname) {
    $liste_dossiers = imap_getmailboxes($inbox, $hostname, "*");
    if (is_array($liste_dossiers)) {
        foreach ($liste_dossiers as $dossier) {
            echo $dossier->name . ", ";
            echo substr($dossier->name, strlen($hostname)) . ", ";
            echo "'" . $dossier->delimiter . "', ";
            echo $dossier->attributes . "<br />\n";
        }
    } else {
        echo "imap_getmailboxes a échoué : " . imap_last_error() . "\n";
    }
}

?>
