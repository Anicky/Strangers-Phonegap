<?php
require_once("../tester-compte.php");
if (isset($liste_boites)) {
    if ($liste_boites) {
        ?>
        <form id="formulaire_boites">
            <table class="formulaire">
                <tr>
                    <td class="label">
                        Chercher dans :
                    </td>
                    <td>
                        <?php
                        foreach ($liste_boites as $boite) {
                            ?>
                            <input type="checkbox" name="folders[]" id="folder_<?php echo $boite; ?>" value="<?php echo $boite; ?>" />
                            <label for="folder_<?php echo $boite; ?>" class="checkbox"><?php echo $boite; ?></label><br />
                            <?php
                        }
                        ?>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Rechercher" />
                    </td>
                </tr>
            </table>
            <input type="hidden" id="serv" name="serv" value="<?php echo htmlspecialchars($_POST['serv']); ?>" />
            <input type="hidden" id="port" name="port" value="<?php echo htmlspecialchars($_POST['port']); ?>" />
            <input type="hidden" id="user" name="user" value="<?php echo htmlspecialchars($_POST['user']); ?>" />
            <input type="hidden" id="pass" name="pass" value="<?php echo htmlspecialchars($_POST['pass']); ?>" />
            <input type="hidden" id="nb" name="nb" value="<?php echo htmlspecialchars($_POST['nb']); ?>" />
            <input type="hidden" id="ssl" name="ssl" value="<?php echo htmlspecialchars($_POST['ssl']); ?>" />
            <input type="hidden" id="num" name="num" value="<?php echo htmlspecialchars($_POST['num']); ?>" />
        </form>
        <script>
            $("#formulaire_boites").submit(function(){
                $("#dialogbox").dialog('open');
                var formData = 'num=' + $('#num').val() +
                    '&params=[{"serv":"' + $('#serv').val() +
                    '","port":"'+ $('#port').val() +
                    '","user":"' + $('#user').val() +
                    '","pass":"'+ $('#pass').val() +
                    '","ssl":"'+ $('#ssl').val() +
                    '","boxes":["INBOX"]}]';
                $.ajax({
                    type: "post",
                    data: formData,
                    url: '../recuperation-mails.php',
                    success: function(d){
                        $("#dialogbox").dialog('close');
                        d = JSON.parse(d);
                        if (d.status == "ok") {
                            $("#conteneur").html("Le numéro appartient à : " + d.name);
                        } else if (d.status == "not found") {
                            $("#conteneur").html("La recherche n'a donné aucun résultat.");
                        } else {
                            $("#conteneur").html("Vérifiez les paramètres de vos comptes emails.");
                        }
                    },
                    error: function(){
                        $("#dialogbox").dialog('close');
                        $("#conteneur").html("Impossible de contacter le serveur.");
                    }
                });
                return false;
            });
        </script>
        <?php
    } else {
        ?>
        <div class="error">
            Erreur : Impossible d'établir une connexion.<br /><br />
            <?php echo imap_last_error(); ?>
        </div>
        <?php
        include_once("formulaire_recherche.php");
    }
}
?>