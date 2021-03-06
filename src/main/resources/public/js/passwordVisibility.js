const toggleOldPassword = document.querySelector("#toggleOldPassword");
const toggleNewPassword = document.querySelector("#toggleNewPassword");
const togglePassword = document.querySelector("#togglePassword");
const toggleConfirmPassword = document.querySelector("#toggleConfirmPassword");
const password = document.querySelector("#password");
const passwordErr = document.querySelector(".passwordErr");
const oldPassword = document.querySelector("#oldPassword");
const newPassword = document.querySelector("#newPassword");
const confirmPassword = document.querySelector("#confirmPassword");
const submitUpdatePassword = document.querySelector("#submitUpdatePassword");
const newChangedPassword = document.querySelector("#newChangePassword");
const confirmUpdatedPassword = document.querySelector("#confirmUpdatePassword");
const confirmChangedPassword = document.querySelector("#confirmChangePassword");


if (toggleNewPassword && newPassword) {
    toggleNewPassword.addEventListener("click", (e) => {
        const type = newPassword.getAttribute("type") === "password" ? "text" : "password";
        newPassword.setAttribute("type", type);
        if(type === "text") {
            toggleNewPassword.classList.remove("fa-eye");
            toggleNewPassword.classList.add("fa-eye-slash");
        } else {
            toggleNewPassword.classList.add("fa-eye");
            toggleNewPassword.classList.remove("fa-eye-slash");
        }
    });
}

if (toggleNewPassword) {
    toggleNewPassword.addEventListener("click", (e) => {
        const type = newChangedPassword.getAttribute("type") === "password" ? "text" : "password";
        newChangedPassword.setAttribute("type", type);
        if(type === "text") {
            toggleNewPassword.classList.remove("fa-eye");
            toggleNewPassword.classList.add("fa-eye-slash");
        } else {
            toggleNewPassword.classList.add("fa-eye");
            toggleNewPassword.classList.remove("fa-eye-slash");
        }
    });
}

if (toggleNewPassword) {
    toggleNewPassword.addEventListener("click", (e) => {
        const type = newUpdatePassword.getAttribute("type") === "password" ? "text" : "password";
        newUpdatePassword.setAttribute("type", type);
        if(type === "text") {
            toggleNewPassword.classList.remove("fa-eye");
            toggleNewPassword.classList.add("fa-eye-slash");
        } else {
            toggleNewPassword.classList.add("fa-eye");
            toggleNewPassword.classList.remove("fa-eye-slash");
        }
    });
}

if (toggleOldPassword) {
    toggleOldPassword.addEventListener("click", (e) => {
        const type = oldPassword.getAttribute("type") === "password" ? "text" : "password";
        oldPassword.setAttribute("type", type);
        if(type === "text") {
            toggleOldPassword.classList.remove("fa-eye");
            toggleOldPassword.classList.add("fa-eye-slash");
        } else {
            toggleOldPassword.classList.add("fa-eye");
            toggleOldPassword.classList.remove("fa-eye-slash");
        }
    });
}

if (togglePassword) {
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
}

if (toggleConfirmPassword && confirmPassword) {
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
}

if (toggleConfirmPassword && confirmUpdatedPassword) {
    toggleConfirmPassword.addEventListener("click", (e) => {
        const type = confirmUpdatedPassword.getAttribute("type") === "password" ? "text" : "password";
        confirmUpdatedPassword.setAttribute("type", type);
        if(type === "text") {
            toggleConfirmPassword.classList.remove("fa-eye");
            toggleConfirmPassword.classList.add("fa-eye-slash");
        } else {
            toggleConfirmPassword.classList.add("fa-eye");
            toggleConfirmPassword.classList.remove("fa-eye-slash");
        }
    });
}

if (toggleConfirmPassword) {
    toggleConfirmPassword.addEventListener("click", (e) => {
        const type = confirmChangedPassword.getAttribute("type") === "password" ? "text" : "password";
        confirmChangedPassword.setAttribute("type", type);
        if(type === "text") {
            toggleConfirmPassword.classList.remove("fa-eye");
            toggleConfirmPassword.classList.add("fa-eye-slash");
        } else {
            toggleConfirmPassword.classList.add("fa-eye");
            toggleConfirmPassword.classList.remove("fa-eye-slash");
        }
    });
}
