let util = {};

util.isMobile = function () {
    return (/android|blackberry|iemobile|ipad|iphone|ipod|opera mini|webos/i.test(navigator.userAgent));
};

export default util;
