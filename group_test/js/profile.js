onload = function () {
    let posts = JSON.parse(localStorage.getItem("posts")) || [];

    for (let i = 0; i < posts.length; i++) {
        if (posts[i].id === (localStorage.getItem("loginingId") || [])) {
            const post = $(`
                <div class="post">
                    <img src="${posts[i].image}" alt="Post image" onclick="location.href='reply.html'">
                    <p class="post-description">${posts[i].text}</p>
                </div>
            `);
            $(".post-section").prepend(post);
        }
    }
};