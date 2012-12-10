<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Strangers (Serveur)</title>
    </head>
    <body>
        <form method="post" action="test_affichage.php">

            <label for="num">Numéro de téléphone</label>
            <input type="text" name="num" id="num" /><br /><br />

            <label for="serv">Serveur</label>
            <input type="text" name="serv" id="serv" /><br /><br />

            <label for="port">Port</label>
            <input type="text" name="port" id="port" value="993" /><br /><br />

            <label for="user">Nom d'utilisateur</label>
            <input type="text" name="user" id="user" /><br /><br />

            <label for="pass">Mot de passe</label>
            <input type="password" name="pass" id="pass" /><br /><br />

            <label for="folder">Dossier</label>
            <input type="text" name="folder" id="folder" value="INBOX" /><br /><br />

            <input type="checkbox" name="ssl" id="ssl" value="1" />
            <label for="ssl">SSL</label>
            <br /><br />

            <input type="submit" value="Valider" />

        </form>
    </body>
</html>
