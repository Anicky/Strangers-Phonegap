<?php include_once("include_header.php"); ?>
<form method="post" action="test_affichage.php">

    <table class="formulaire">
        <tr>
            <td class="label">
                <label for="num">Numéro de téléphone</label>
            </td>
            <td>
                <input type="text" name="num" id="num" />
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="serv">Serveur</label>
            </td>
            <td>
                <input type="text" name="serv" id="serv" />
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="port">Port</label>
            </td>
            <td>
                <input type="text" name="port" id="port" value="993" />
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="user">Nom d'utilisateur</label>
            </td>
            <td>
                <input type="text" name="user" id="user" />
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="pass">Mot de passe</label>
            </td>
            <td>
                <input type="password" name="pass" id="pass" />
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="folder">Dossier</label>
            </td>
            <td>
                <input type="text" name="folder" id="folder" value="INBOX" />
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="folder">Nombre d'emails à récupérer<br />(A partir du + récent / Mettre 0 pour "tous")</label>
            </td>
            <td>
                <input type="text" name="nb" id="nb" value="0" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="checkbox" name="ssl" id="ssl" value="1" />
                <label for="ssl" class="checkbox">SSL</label>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Valider" />
            </td>
        </tr>
    </table>

</form>
<?php include_once("include_footer.php"); ?>