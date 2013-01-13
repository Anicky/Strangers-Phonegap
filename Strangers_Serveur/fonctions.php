<?php

/**pe $server L'adresse du serveur
 * @param type $port Le numéro de port
 * @param type $ssl Indique si la connexion est en SSL
 * @param type $folder Le dossier à ouvrir
 * @return type
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

function get_boites($hostname, $username, $password) {
    $retour = false;
    $inbox = imap_open($hostname, $username, $password);
    if ($inbox) {
        $liste_boites = imap_list($inbox, $hostname, "*");
        if (is_array($liste_boites)) {
            for ($i = 0; $i < count($liste_boites); $i++) {
                $liste_boites[$i] = substr($liste_boites[$i], strlen($hostname));
            }
            $retour = remplace_accents_imap($liste_boites);
        }
    }
    return $retour;
}

function remplace_accents_imap($objet) {
    $accents = array('&-' => '&',
        '&AOk-' => 'é',
        '&AOI-' => 'â',
        '&AOA-' => 'à',
        '&AOg-' => 'è',
        '&AOc-' => 'ç',
        '&APk-' => 'ù',
        '&AOo-' => 'ê',
        '&AO4-' => 'î',
        '&APM-' => 'ó',
        '&APE-' => 'ñ',
        '&AOE-' => 'á',
        '&APQ-' => 'ô',
        '&AMk-' => 'É',
        '&AOs-' => 'ë');
    return str_replace(array_keys($accents), array_values($accents), $objet);
}

?>
