const togglePassword = document.querySelector("#togglePassword");
const toggleConfirmPassword = document.querySelector("#toggleConfirmPassword");
const password = document.querySelector("#password");
const confirmPassword = document.querySelector("#confirmPassword");
togglePassword.addEventListener("click", (e) => {
    const type = password.getAttribute("type") === "password" ? "text" : "password";
    password.setAttribute("type", type);
    if(type === "text") {
        togglePassword.classList.remove("fa-eye");
        togglePassword.classList.add("fa-eye-slash");
    } else {
        togglePassword.classList.add("fa-eye");
        togglePassword.classList.remove("fa-eye-slash");
    }
});
toggleConfirmPassword.addEventListener("click", (e) => {
    const type = confirmPassword.getAttribute("type") === "password" ? "text" : "password";
    confirmPassword.setAttribute("type", type);
    if(type === "text") {
        toggleConfirmPassword.classList.remove("fa-eye");
        toggleConfirmPassword.classList.add("fa-eye-slash");
    } else {
        toggleConfirmPassword.classList.add("fa-eye");
        toggleConfirmPassword.classList.remove("fa-eye-slash");
    }
});
