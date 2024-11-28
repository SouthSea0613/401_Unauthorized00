function findId() {
  let email = document.getElementById("email").value.trim();
  let resultDiv = document.getElementById("result");

  if (email === "") {
    alert("이메일을 입력해주세요.");
    return;
  }

  let accounts = JSON.parse(localStorage.getItem("accounts") || "[]");

  let found = false;

  accounts.forEach((account) => {
    if (account.email === email) {
      alert(`아이디는:${account.idset} 입니다.`);
      found = true;
    }
  });

  if (!found) {
    resultDiv.innerHTML = "<p>해당 이메일에 등록된 아이디가 없습니다.</p>";
  }
}
