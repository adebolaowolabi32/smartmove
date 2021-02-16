const updatePasswordForm = document.querySelector("#updatePasswordForm");
const changePasswordForm = document.querySelector("#changePasswordForm");
const newUpdatePassword = document.querySelector("#newUpdatePassword");
const newChangePassword = document.querySelector("#newChangePassword");
const confirmUpdatePassword = document.querySelector("#confirmUpdatePassword");
const confirmChangePassword = document.querySelector("#confirmChangePassword");
const passwordConfirmationErr = document.querySelector(".passwordErr");


if (updatePasswordForm) {
    updatePasswordForm.addEventListener('submit', (e) => {
        if (newUpdatePassword.value !== confirmUpdatePassword.value) {
            e.preventDefault();
            passwordConfirmationErr.classList.add("alert");
            passwordConfirmationErr.classList.add("alert-danger");
            newUpdatePassword.value = ""
            confirmUpdatePassword.value = ""
            passwordConfirmationErr.textContent = "Oops... passwords do not match";
            setTimeout(() => {
                passwordConfirmationErr.textContent = "";
                passwordConfirmationErr.classList.remove("alert");
                passwordConfirmationErr.classList.remove("alert-danger");
            }, 5000);
        }
    })
}

if (changePasswordForm) {
    changePasswordForm.addEventListener('submit', (e) => {
        if (newChangePassword.value !== confirmChangePassword.value) {
            e.preventDefault();
            passwordConfirmationErr.classList.add("alert");
            passwordConfirmationErr.classList.add("alert-danger");
            newChangePassword.value = ""
            confirmChangePassword.value = ""
            passwordConfirmationErr.textContent = "Oops... passwords do not match";
            setTimeout(() => {
                passwordConfirmationErr.textContent = "";
                passwordConfirmationErr.classList.remove("alert");
                passwordConfirmationErr.classList.remove("alert-danger");
            }, 5000);
        }
    })
}
