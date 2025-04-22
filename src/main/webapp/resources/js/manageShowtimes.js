/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".form-check-input").forEach(input => {
        input.addEventListener("change", function() {
            console.log("Showtime selected:", this.value);
        });
    });
});
