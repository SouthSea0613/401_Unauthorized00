function setclick() {
    let accounts = JSON.parse(localStorage.getItem("accounts")) || [];

    let idset = document.getElementById("idset").value;
    let pwset = document.getElementById("pwset").value;
    let pwcheck = document.getElementById("pwsetcheck").value;
    let name = document.getElementById("nameset").value;
    let nikname = document.getElementById("nikname").value;
    let email = document.getElementById("emailset").value;
    let phone = document.getElementById("phoneset").value;
    let date = document.getElementById("bithset");
    let selecteddate = date.value;
    let gender = document.querySelector('input[name="genderset"]:checked');

    if (idset === "") {
        alert("아이디를 입력해주세요");
        document.getElementById("idset").focus();
        return;
    }
    else if (pwset === "") {
        alert("비밀번호를 입력해주세요");
        document.getElementById("pwset").focus();
        return;
    }
    else if (pwcheck === "") {
        alert("비밀번호 확인란이 입력되지 않았습니다");
        document.getElementById("pwsetcheck").focus();
        return;
    }
    else if (name === "") {
        alert("성명을 입력해주세요");
        document.getElementById("nameset").focus();
        return;
    }
    else if (nikname === "") {
        alert("사용자 이름(닉네임)을 입력해주세요");
        document.getElementById("nikname").focus();
        return;
    }
    else if (email === "") {
        alert("이메일을 입력해주세요");
        document.getElementById("emailset").focus();
        return;
    }
    else if (phone === "") {
        alert("전화번호를 입력해주세요");
        document.getElementById("phoneset").focus();
        return;
    }
    else if (selecteddate === "") {
        alert("날짜가 선택되지 않았습니다.");
        document.getElementById("bithset").focus();
        return;
    }
    else if (!gender) {
        alert("성별이 입력되지않았습니다.");
    }
    else if (idset.length < 4 || idset.length > 10) {
        alert("아이디의 길이가 충족하지 못했습니다");
        document.getElementById("idset").focus();
        return;
    }
    else if (pwset.length < 8 || pwset.length > 14) {
        alert("비밀번호의 길이가 충족하지 못했습니다");
        document.getElementById("pwset").focus();
        return;
    }
    else if (document.getElementById("pwsetcheck").value !== pwset) {
        alert("비밀번호가 일치하지 않습니다");
        document.getElementById("pwset").focus();
        return;
    }
    else {
        let userdata = {
            idset: idset,
            pwset: pwset,
            name: name,
            nikname: nikname,
            email: email,
            phone: phone,
            selecteddate: selecteddate,
            gender: gender.value
        }

        accounts.push(userdata);
        localStorage.setItem('accounts', JSON.stringify(accounts));
        alert("회원가입 성공");
        location.reload();
    }
}