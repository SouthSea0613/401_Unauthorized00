$(document).ready(function () {
  $("#resetBtn").click(function () {
    resetPassword();
  });
});
function resetPassword() {
  let email = $("#email").val().trim();
  let newPassword = $("#newPassword").val().trim();
  let confirmPassword = $("#confirmPassword").val().trim();

  if (email === "" || newPassword === "" || confirmPassword === "") {
    alert("이메일, 새 비밀번호 및 비밀번호 확인을 모두 입력해주세요.");
    return;
  }

  if (newPassword !== confirmPassword) {
    alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    $("#newPassword").focus();
    return;
  }
  if (newPassword.length < 8 || newPassword.length > 14) {
    alert("비밀번호의 길이는 8~14자 입니다.");
    $("#newPassword").focus();
    return;
  }
  if (confirmPassword.length < 8 || confirmPassword.length > 14) {
    alert("비밀번호의 길이는 8~14자 입니다.");
    $("#confirmPassword").focus();
    return;
  }

  let accounts = JSON.parse(localStorage.getItem("accounts") || "[]");

  let found = false;

  $.each(accounts, function (index, account) {
    if (account.email === email) {
      account.pwset = newPassword;
      localStorage.setItem("accounts", JSON.stringify(accounts));
      alert("비밀번호가 성공적으로 재설정되었습니다.");
      alert("로그인 창으로 이동합니다.");
      location.href = "login.html";
      found = true;
      return false;
    }
  });

  if (!found) {
    alert("해당 이메일에 등록된 계정이 없습니다.");
  }
}
