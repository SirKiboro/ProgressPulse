const API_URL = "/api/v1/users";

async function registerUser(event) {
    event.preventDefault();

    const nameValue = document.getElementById('name').value;
        const emailValue = document.getElementById('email').value;
        const passwordValue = document.getElementById('password').value;
        const genderValue = document.getElementById('gender').value;
        const heightValue = document.getElementById('heightCm').value;
        const dobValue = document.getElementById('dateOfBirth').value;

    const userData = {
       name: nameValue,
       email: emailValue,
       password: passwordValue,
       gender: genderValue.toUpperCase(),
       heightCm: parseFloat(heightValue),
       dateOfBirth: dobValue
    };

    console.log("Registration button clicked! Data being sent...", userData);

    // If password is blank...
        if (!userData.password) {
            alert("Password field is empty in the form!");
            return;
        }

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            alert("Registration successful!");
            document.getElementById('userRegistration').reset();
        } else {
            const errorText = await response.text();
            alert("Server Error: " + errorText);
        }
    } catch (err) {
        console.error("Connection failed:", err);
        alert("Could not connect to the server.");
    }
}


document.getElementById('userRegistration').addEventListener('submit', registerUser);