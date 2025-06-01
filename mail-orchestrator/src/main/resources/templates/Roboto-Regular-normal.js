(function (jsPDFAPI) {
    var RobotoRegular =
        'AAEAAAASAQAABAAgR0RFRrRCsIIAAAEg…(очень длинная строка Base64)…';

    jsPDFAPI.addFileToVFS('Roboto-Regular.ttf', RobotoRegular);
    jsPDFAPI.addFont('Roboto-Regular.ttf', 'Roboto-Regular', 'normal');
})(window.jspdf.jsPDF.API);