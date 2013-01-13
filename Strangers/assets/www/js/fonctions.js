var URL_SERVER = "https://192.168.1.11/strangers/";

$('#options').live('pagecreate', function(event){
    showAccounts();
});

$('#journal-appels').live('pagecreate', function(event){
    showCallList();
});

$("#comptes-ajouter").live("pagecreate", function(event){
    var params = parseURLParams($(this).attr("data-url"));
    var id = null;
    if (params != null) {
        id = JSON.parse(params["id"]);
    }
    editAccount(id);
});

function parseURLParams(url) {
    var queryStart = url.indexOf("?") + 1;
    var queryEnd   = url.indexOf("#") + 1 || url.length + 1;
    var query      = url.slice(queryStart, queryEnd - 1);

    var params = null;
    if (query !== url && query !== "") {
        params  = {};
        var nvPairs = query.replace(/\+/g, " ").split("&");

        for (var i=0; i<nvPairs.length; i++) {
            var nv = nvPairs[i].split("=");
            var n  = decodeURIComponent(nv[0]);
            var v  = decodeURIComponent(nv[1]);
            if ( !(n in params) ) {
                params[n] = [];
            }
            params[n].push(nv.length === 2 ? v : null);
        }
    }
    return params;
}

function showCallList() {
    cordova.exec(
        function(listeAppels) {
            var html = "";
            for (var i = 0; i < listeAppels.length; i++) {
                html += '<li><a href="../index.html" onclick="addNumber(\'' + listeAppels[i] + '\')">' + listeAppels[i] + '</a></li>';
            }
            $("#listeAppels").html(html);
        }, function() {
            alert("Une erreur est survenue : le journal d'appels est indisponible.");
        },
        "JournalAppels",
        "list",
        [""]);
}

function setAccount(json_args) {
    var ok = false;
    cordova.exec(
        function(retour) {
            ok = true;
        }, function(error) {
            alert(error);
        },
        "StockageLocal",
        "set",
        [json_args]);
    return ok;
}

function getAccounts() {
    var comptes = null;
    cordova.exec(
        function(listeComptes) {
            comptes = listeComptes;
        }, function(error) {
            alert("Une erreur est survenue : impossible de récupérer les comptes emails.");
        },
        "StockageLocal",
        "get",
        [""]);
    return comptes;
}

function showAccounts() {
    var comptes = getAccounts();
    var html = "";
    for (var i = 0; i < comptes.length; i++) {
        if (comptes[i] != null) {
            html += '<li><a href="comptes-ajouter.html?id=' + i + '">' + comptes[i]['mail'] + '</a><a href="#" onclick="deleteAccount(' + i + ')"></a>';  
        }
    }
    $("#listeComptes").html(html);
}

function addNumber(numero) {
    $("#accueil_telephone").val(numero);
}

function editAccount(id) {
    if (id == null) {
        $("#compte_email").val("");
        $("#compte_user").val("");
        $("#compte_pass").val("");
        $("#compte_server").val("");
        $("#compte_port").val("993");
        $("#compte_ssl").val("0");
    } else {
        cordova.exec(
            function(compte) {
                $("#compte_email").val(compte['mail']);
                $("#compte_user").val(compte['user']);
                $("#compte_pass").val(compte['pass']);
                $("#compte_server").val(compte['serv']);
                $("#compte_port").val(compte['port']);
                $("#compte_ssl").val(compte['ssl']);
            }, function(error) {
                alert("Une erreur est survenue : impossible de récupérer le compte email.");
            },
            "StockageLocal",
            "get",
            [id]);
    }
}

function deleteAccount(id) {
    navigator.notification.confirm(
        'Etes-vous sûr de vouloir supprimer ce compte email ?',
        function(button) {
            if (button == 2) {
                cordova.exec(
                    function(ok) {
                        showAccounts();
                        $("#listeComptes").listview("refresh");
                    }, function(error) {
                        alert("Une erreur est survenue : impossible de supprimer le compte email.");
                    },
                    "StockageLocal",
                    "delete",
                    [id]);
            }
        },
        'Suppression',
        'Non,Oui'
        );
}
