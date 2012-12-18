document.addEventListener("deviceready", onDeviceReady, false);
function onDeviceReady() {
// Empty
}
function showCallList() {
    cordova.exec(
        function(listeAppels) {
            var html = "";
            for (i = 0; i < listeAppels.length; i++) {
                html += "<li><a href=\"#accueil\" data-transition=\"slide\" onclick=\"addNumber(" + listeAppels[i] + ")\">" + listeAppels[i] + "</a></li>";
            }
            $("#listeAppels").html(html);
            $("#listeAppels").listview("refresh");
        }, function() {
            alert("Une erreur est survenue : le journal d'appels est indisponible.");
        },
        "JournalAppels",
        "list",
        [""]);
}
function debug_set() {
    cordova.exec(
        function(retour) {
            alert(retour);
        }, function(error) {
            alert(error);
        },
        "StockageLocal",
        "set",
        [
        {
            email:'martin.dupont@gmail.com', 
            user:'martin.dupont', 
            pass:'test1', 
            server:'iamp.googlemail.com', 
            port:93, 
            ssl:true
        },

        {
            email:'jeanne.darc@free.fr', 
            user:'ja', 
            pass:'test2', 
            server:'imap.free.fr', 
            port:62, 
            ssl:false
        }
        ]);
}
function getAccounts() {
    cordova.exec(
        function(listeComptes) {
            var html = "";
            for (i = 0; i < listeComptes.length; i++) {
                html += "<li><a href=\"#comptes-ajouter\" data-transition=\"slide\">" + listeComptes[i]['email'] + "</a></li>";
            }
            $("#listeComptes").html(html);
            $("#listeComptes").listview("refresh");
        }, function(error) {
            alert("Une erreur est survenue : impossible de récupérer les comptes emails.");
        },
        "StockageLocal",
        "get",
        [""]);
}
function addNumber(numero) {
    $("#accueil_telephone").val(numero);
}

