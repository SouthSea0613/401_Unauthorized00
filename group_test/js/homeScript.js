onload = function() {
    let posts = JSON.parse(localStorage.getItem("posts")) || [];
    for (let i = 0; i < posts.length; i++) {
        const post = $(`
            <div class="post">
                <img src="${posts[i].image}" alt="Post image">
                <p class="post-description">${posts[i].text}</p>
            </div>
        `);
        $(".post-section").prepend(post);
    }

    $('.post-section .post').click(function () {
        localStorage.setItem("img", $(this).children('img').attr('src'));
        localStorage.setItem('contents', $(this).children('.post-description').html());
        location.href='reply.html'
    })

}

$(".commit-button").click(()=> {
    let posts = JSON.parse(localStorage.getItem("posts")) || [];

    let id = localStorage.getItem("loginingId") || [];
    let input1 = $(".post input").eq(0).val();
    let input2 = $(".post input").eq(1).val();

    let post = {
        id: id,
        image: input1,
        text: input2
    }

    posts.push(post);

    localStorage.setItem("posts", JSON.stringify(posts));

    $(".post-section").text('');
    if (input1 || input2) {
        for (let i = 0; i < posts.length; i++) {
            const post = $(`
            <div class="post">
                <img src="${posts[i].image}" alt="Post image">
                <p class="post-description">${posts[i].text}</p>
            </div>
        `);
            $(".post-section").prepend(post);
        }

        $(".post input").eq(0).val("");
        $(".post input").eq(1).val("");
        window.scroll(top);
    }
    else {
        alert("입력값이 없습니다. 내용을 입력해주세요.");
    }
});

$($(".post input")[1]).change(()=> {
    let posts = JSON.parse(localStorage.getItem("posts")) || [];

    let id = localStorage.getItem("loginingId") || [];
    let input1 = $(".post input").eq(0).val();
    let input2 = $(".post input").eq(1).val();

    let post = {
        id: id,
        image: input1,
        text: input2
    }

    posts.push(post);

    localStorage.setItem("posts", JSON.stringify(posts));

    $(".post-section").text('');
    if (input1 || input2) {
        for (let i = 0; i < posts.length; i++) {
            const post = $(`
            <div class="post">
                <img src="${posts[i].image}" alt="Post image">
                <p class="post-description">${posts[i].text}</p>
            </div>
        `);
            $(".post-section").prepend(post);
        }

        $(".post input").eq(0).val("");
        $(".post input").eq(1).val("");
        window.scroll(top);
    }
    else {
        alert("입력값이 없습니다. 내용을 입력해주세요.");
    }
});

function LogOut() {
    if (confirm("로그아웃 하시겠습니까?")) {
        localStorage.setItem('loginingId', '');
        location.href = 'login.html';
    }
}