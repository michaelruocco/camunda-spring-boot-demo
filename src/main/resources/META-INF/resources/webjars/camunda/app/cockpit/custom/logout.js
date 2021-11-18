let observer = new MutationObserver(() => {
  const logoutButton = document.querySelectorAll(".logout > a")[0];
  if (logoutButton) {
    var parent = logoutButton.parentElement;
    parent.removeChild(logoutButton);
    var newLogout = document.createElement("a");
    newLogout.setAttribute("className", "ng-binding");
    newLogout.innerText = logoutButton.innerText.replaceAll("\n", "");
    newLogout.setAttribute("href", "logout");
    parent.appendChild(newLogout);
    observer.disconnect();
  }
});

observer.observe(document.body, {
  childList: true,
  subtree: true,
  attributes: false,
  characterData: false
});