window.calllog = function(str, callback) {
    cordova.exec(callback, function(err) {
        callback('Aucun appel.');
    }, "JeremieTest", "list", [str]);
};