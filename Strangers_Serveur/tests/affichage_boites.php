<?php
require_once("../tester-compte.php");
if (isset($liste_boites)) {
    if ($liste_boites) {
        ?>
            <form method="post" action="affichage_mails.php">
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
                <input type="hidden" name="serv" value="<?php echo htmlspecialchars($_POST['serv']); ?>" />
                <input type="hidden" name="port" value="<?php echo htmlspecialchars($_POST['port']); ?>" />
                <input type="hidden" name="user" value="<?php echo htmlspecialchars($_POST['user']); ?>" />
                <input type="hidden" name="pass" value="<?php echo htmlspecialchars($_POST['pass']); ?>" />
                <input type="hidden" name="nb" value="<?php echo htmlspecialchars($_POST['nb']); ?>" />
                <input type="hidden" name="ssl" value="<?php echo htmlspecialchars($_POST['ssl']); ?>" />
                <input type="hidden" name="num" value="<?php echo htmlspecialchars($_POST['num']); ?>" />
            </form>
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