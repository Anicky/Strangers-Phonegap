<form method="post" action="affichage_boites.php" id="formulaire_recherche">

    <table class="formulaire">
        <tr>
            <td class="label">
                <label for="serv">Serveur</label>
            </td>
            <td>
                <input type="text" name="serv" id="serv" size="40" />
            </td>
            <td>
                <label class="error" for="serv">Vous devez indiquer l'adresse du serveur.</label>
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="port">Port</label>
            </td>
            <td>
                <input type="number" name="port" id="port" value="993" size="5" />
            </td>
            <td>
                <label class="error" for="port">Vous devez indiquer le numéro de port (par défaut à 993 pour IMAP).</label>
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="user">Nom d'utilisateur</label>
            </td>
            <td>
                <input type="text" name="user" id="user" size="20" />
            </td>
            <td>
                <label class="error" for="user">Vous devez indiquer votre nom d'utilisateur (par défaut votre adresse email).</label>
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="pass">Mot de passe</label>
            </td>
            <td>
                <input type="password" name="pass" id="pass" size="20" />
            </td>
            <td>
                <label class="error" for="pass">Vous devez indiquer votre mot de passe.</label>
            </td>
        </tr>
        <tr>
            <td class="label">
                <label for="nb">Nombre d'emails à récupérer<br />(A partir du + récent / Mettre 0 pour "tous")</label>
            </td>
            <td>
                <input type="number" name="nb" id="nb" value="0" size="5" />
            </td>
            <td></td>
        </tr>
        <tr>
            <td colspan="2" class="colspan">
                <input type="checkbox" name="ssl"  checked="yes" id="ssl" value="1" />
                <label for="ssl" class="checkbox" checked="yes" >SSL</label>
            </td>
            <td></td>
        </tr>
        <tr>
            <td class="label">
                <label for="num">Numéro de téléphone</label>
            </td>
            <td>
                <input type="tel" name="num" id="num" size="20" />
            </td>
            <td></td>
        </tr>
        <tr>
            <td colspan="2" class="colspan">
                <input type="submit" value="Tester la connexion" />
            </td>
            <td></td>
        </tr>
    </table>

</form>
<div id="dialogbox">
    <?php include("../includes/loading.php"); ?>
</div>
<script>
    $("#dialogbox").dialog({
        autoOpen : false,
        modal : true,
        resizable : false,
        draggable : false,
        show : "fade",
        hide : "fade",
        title : "Chargement",
        width : 20,
        height : 80,
        open: function(event, ui) {
            $(this).closest('.ui-dialog').find('.ui-dialog-titlebar-close').hide();
        }
    });
    $("#formulaire_recherche").validate({
        rules: {
            serv : {
                required : true
            },
            port : {
                required : true
            },
            user : {
                required : true
            },
            pass : {
                required : true
            }
        }
    });
    $("#formulaire_recherche").ajaxForm({
        target : "#conteneur",
        beforeSubmit : function() {
            $("#dialogbox").dialog('open');
        },
        success : function() {
            $("#dialogbox").dialog('close');
        }
    });
</script>