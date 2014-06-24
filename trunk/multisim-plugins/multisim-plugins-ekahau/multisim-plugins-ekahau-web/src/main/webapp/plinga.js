plingaRpc = {

    init : function(callback) {
        callback();
    },

    ready: function(callback) {
        if (typeof callback === 'function') {
        callback();
        }
        // Some games don't call adjustHeight after loading
        window.setTimeout(plingaRpc.adjustHeight, 2000);
        window.setTimeout(plingaRpc.adjustHeight, 5000);
        window.setTimeout(plingaRpc.adjustHeight, 10000);
        window.setTimeout(plingaRpc.adjustHeight, 25000);
        },

    getOwner: function() {
        var result = jQuery.ajax({
            async: false,
            type: "POST",
            url: "http://localhost:58080/multisim-plugins-plinga-web/system/?simaction=getowner"
        });
        return result.responseText;
    },

    getFriends: function() {
        var result = jQuery.ajax({
            async: false,
            type: "POST",
            url: "http://localhost:58080/multisim-plugins-plinga-web/system/?simaction=getfriends"
        });
        return JSON.parse(result.responseText);
    },

    initCoins: function(vcurrency, customparam) {
        window.location.replace(
                "http://localhost:58080/multisim-plugins-plinga-web/payment.jsp"
        );
    },

    post: function(message, callback) {
        $('#clientContainer').css("left", -10000);

        var iframe = $('<iframe />')
            .attr('src', 'http://localhost:58080/multisim-plugins-plinga-web/post.jsp?'
                + "&target=" + message.target
                + "&title=" + message.title
                + "&body=" + message.body
                + "&pic=" + message.pic
                + "&linktext=" + message.linktext
                + "&params=" + message.params)
            .attr('id', 'postframe');

        var frame = document.getElementById('postframe');

        $('#plingaDebug').css("display", "block").append(iframe);

        window.addEventListener("message", receiveMessage, false);
        function receiveMessage(event) {
            alert("received: " + event);
        }

        frame.contentWindow.postMessage("postframe", "postframe");

        if (typeof callback === 'function') {
            callback({globalError_:false});
        }
    },

    invite: function(message, params, callback) {
        plingaRpc.plinga.invite(message, params, callback);
        },

    reload: function() {
        plingaRpc.plinga.reload();
        },

    adjustHeight: function(height) {
        if (height) {
        plingaRpc.last_height = height;
        }

        if (plingaRpc.last_height) {
        // Define height
        height = plingaRpc.last_height;
        }
        else {
        // Compute height
        var b = document.body, e = document.documentElement;
        height = Math.max(
        Math.max(b.scrollHeight, e.scrollHeight),
        Math.max(b.offsetHeight, e.offsetHeight),
        Math.max(b.clientHeight, e.clientHeight)
        );
        }
        plingaRpc.plinga.adjustHeight(height);
        },

    request: function(url, params, callback) {
        plingaRpc.plinga.request(url, params, callback);
        },

    getUsers: function(userIds, callback) {
        plingaRpc.plinga.getUsers(userIds, callback);
        },

    sendMessage: function(message, callback) {
        plingaRpc.plinga.sendMessage(message, callback);
        },

    levelUp: function(levelNumber) {
        plingaRpc.plinga.levelUp(levelNumber);
        },

    resize: function(width, height) {
        plingaRpc.plinga.resize(width, height);
        },

    getUrl: function(params) {
        plingaRpc.plinga.getUrl(params);
        },

    goToGame: function(bundleData) {
        plingaRpc.plinga.goToGame(bundleData);
        },

    redirect: function(url, params) {
        if (typeof params === "object") {
        delete params['xdm_c'];
        delete params['xdm_e'];
        delete params['xdm_p'];
        }
        plingaRpc.plinga.redirect(url, params);
        },

    showContactForm: function() {
        plingaRpc.plinga.showContactForm();
        },

    showHelp : function() {
        plingaRpc.plinga.showHelp();
        },

    trackInGame : function(type, value) {
        plingaRpc.plinga.trackInGame(type, value);
        },

    requestOnGameAd: function(bundleData) {
        plingaRpc.plinga.requestOnGameAd(bundleData);
        }
};

function onClosePostWindow() {
    $('#clientContainer').css("left", 0);
}