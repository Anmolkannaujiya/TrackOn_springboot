const BASE_URL = "http://localhost:8080";

export async function loginUser(email, password){
    const response = await fetch(`${BASE_URL}/api/auth/login`,{
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            email,
            password
        })
    });

    if(!response.ok){
        throw new Error("Login failed");
    }

    return  response.json();
}

export async function getTasks() {

    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/api/tasks`, {
        method: "GET",

        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        throw new Error("Failed to fetch tasks");
    }

    return response.json();
}

export async function createTask(taskData) {

    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/api/tasks`, {
        method: "POST",

        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },

        body: JSON.stringify(taskData)
    });

    if (!response.ok) {
        throw new Error("Failed to create task");
    }

    return response.json();
}

export async function deleteTask(id) {

    const token = localStorage.getItem("token");

    const response = await fetch(
        `${BASE_URL}/api/tasks/${id}`,
        {
            method: "DELETE",

            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    if (!response.ok) {
        throw new Error("Failed to delete task");
    }
}

export async function updateTask(id, taskData) {

    const token = localStorage.getItem("token");

    const response = await fetch(
        `${BASE_URL}/api/tasks/${id}`,
        {
            method: "PUT",

            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },

            body: JSON.stringify(taskData)
        }
    );

    if (!response.ok) {
        throw new Error("Failed to update task");
    }

    return response.json();
}

export async function registerUser(username, email, password) {

    const response = await fetch(`${BASE_URL}/api/auth/register`, {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            username,
            email,
            password
        })
    });

    if (!response.ok) {
        throw new Error("Registration failed");
    }

    return response.text();
}