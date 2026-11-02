(function() {
    var disabled = document.getElementById('unblocked-disabled')
    var signature = document.getElementById('unblocked-signature')

    function toggleSignature() {
        signature.style.display = disabled.checked ? 'none' : ''
    }

    disabled.addEventListener('change', toggleSignature)

    toggleSignature()
})();
