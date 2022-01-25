$(function(){

    $("ul.tabs li").click(function(){
        var tabId = $(this).attr("data-tab");

        $("ul.tabs li").removeClass("current");
        $(".tab-content").removeClass("current");

        $(this).addClass("current");
        $("#"+ tabId).addClass("current");
    });

    $('#summernote').summernote({
        lang: 'ko-KR',  // 메뉴 한글 버전 ,
        minHeight: 400, // 최소 높이
        maxHeight: null,
        placeholder: "내용 입력"

    });

});

/*
var input = document.getElementById("pinput");
var initLabel = document.getElementById("plabel");


$("#pinput").addEventListener("change", (event) => {
  const files = changeEvent(event);
  handleUpdate(files);
});

initLabel.addEventListener("mouseover", (event) => {
  event.preventDefault();
  const label = document.getElementById("plabel");
  label?.classList.add("plabel--hover");
});

initLabel.addEventListener("mouseout", (event) => {
  event.preventDefault();
  const label = document.getElementById("plabel");
  label?.classList.remove("plabel--hover");
});

document.addEventListener("dragenter", (event) => {
  event.preventDefault();
  console.log("dragenter");
  if (event.target.className === "inner") {
    event.target.style.background = "#616161";
  }
});

document.addEventListener("dragover", (event) => {
  console.log("dragover");
  event.preventDefault();
});

document.addEventListener("dragleave", (event) => {
  event.preventDefault();
  console.log("dragleave");
  if (event.target.className === "pinner") {
    event.target.style.background = "#3a3a3a";
  }
});

document.addEventListener("drop", (event) => {
  event.preventDefault();
  console.log("drop");
  if (event.target.className === "pinner") {
    const files = event.dataTransfer?.files;
    event.target.style.background = "#3a3a3a";
    handleUpdate([...files]);
  }
});

function changeEvent(event){
  const { target } = event;
  return [...target.files];
};

function handleUpdate(fileList){
  const preview = document.getElementById("preview");

  fileList.forEach((file) => {
    const reader = new FileReader();
    reader.addEventListener("load", (event) => {
      const img = el("img", {
        className: "embed-img",
        src: event.target?.result,
      });
      const imgContainer = el("div", { className: "container-img" }, img);
      preview.append(imgContainer);
    });
    reader.readAsDataURL(file);
  });
};

function el(nodeName, attributes, ...children) {
  const node =
    nodeName === "fragment"
      ? document.createDocumentFragment()
      : document.createElement(nodeName);

  Object.entries(attributes).forEach(([key, value]) => {
    if (key === "events") {
      Object.entries(value).forEach(([type, listener]) => {
        node.addEventListener(type, listener);
      });
    } else if (key in node) {
      try {
        node[key] = value;
      } catch (err) {
        node.setAttribute(key, value);
      }
    } else {
      node.setAttribute(key, value);
    }
  });

  children.forEach((childNode) => {
    if (typeof childNode === "string") {
      node.appendChild(document.createTextNode(childNode));
    } else {
      node.appendChild(childNode);
    }
  });

  return node;
}*/
