// global array to store scroll values
var scrollValues = new Array();

// We're using a global variable to store the number of occurrences
var MyApp_SearchResultCount = 0;

// the main entry point to start the search
function MyApp_HighlightAllOccurencesOfString(keyword) {
    MyApp_RemoveAllHighlights();
    MyApp_HighlightAllOccurencesOfStringForElement(document.body, keyword.toLowerCase());
}

function getSearchScrollValues() {
	var x = getElementsByClassName("MyAppHighlight", document.body);
	for (var i=0; i<=x.length-1; i++) {
		scrollValues[i]=x[i].offsetTop;
		// MainActivity.showLog("x["+i+"]: "+x[i].offsetTop);
	}
	
	var result = JSON.stringify(scrollValues);
	MainActivity.pushScrollValue(result);
	
	// showToast(result);
	// showLog(result);
	//alert(result);
	return result;
}

// helper function, recursively searches in elements and their child nodes
function MyApp_HighlightAllOccurencesOfStringForElement(element,keyword) {
	if (element) {
		if (element.nodeType == 3) {        // 3 is a Text node
			while (true) {
				// If the object is a TextNode, the nodeValue property returns a string representing the text contained by the node.
				var value = element.nodeValue;  // Search for keyword in text node and returns the paragraph its in
				var idx = value.toLowerCase().indexOf(keyword);
				
				if (idx < 0) break;             // not found, abort
				var span = document.createElement("span");
				var text = document.createTextNode(value.substr(idx,keyword.length));
				span.appendChild(text);
				span.setAttribute("class","MyAppHighlight");
				span.style.backgroundColor="yellow";
				span.style.color="black";
				text = document.createTextNode(value.substr(idx+keyword.length));
				element.deleteData(idx, value.length - idx);
				var next = element.nextSibling;
				element.parentNode.insertBefore(span, next);
				element.parentNode.insertBefore(text, next);
				element = text;
				MyApp_SearchResultCount++;	// update the counter
			}//while
		} else if (element.nodeType == 1) { // Element node
			if (element.style.display != "none" && element.nodeName.toLowerCase() != 'select') {
				for (var i=element.childNodes.length-1; i>=0; i--) {
					MyApp_HighlightAllOccurencesOfStringForElement(element.childNodes[i],keyword);
				}//for
			}// (element.style.display != "none" && element.nodeName.toLowerCase() != 'select')
		}//else if (element.nodeType == 1)
	}//if (element)
}

function getElementsByClassName(classname, node)  {
	if(!node) node = document.getElementsByTagName("body")[0];
	var a = [];
	var re = new RegExp('\\b' + classname + '\\b');
	var els = node.getElementsByTagName("*");
	for(var i=0,j=els.length; i<j; i++)
		if(re.test(els[i].className))a.push(els[i]);
	return a;
}

// helper function, recursively removes the highlights in elements and their childs
function MyApp_RemoveAllHighlightsForElement(element) {
	if (element) {
		if (element.nodeType == 1) {
			if (element.getAttribute("class") == "MyAppHighlight") {
				var text = element.removeChild(element.firstChild);
				element.parentNode.insertBefore(text,element);
				element.parentNode.removeChild(element);
				return true;
			} else {
				var normalize = false;
				for (var i=element.childNodes.length-1; i>=0; i--) {
					if (MyApp_RemoveAllHighlightsForElement(element.childNodes[i])) {
						normalize = true;
					}
				}
				if (normalize) {
					element.normalize();
				}
			}
		}
	}
	return false;
}

// the main entry point to remove the highlights
function MyApp_RemoveAllHighlights() {
	MyApp_SearchResultCount = 0;
	// emptyScrollValues();
	MyApp_RemoveAllHighlightsForElement(document.body);
}

function portraitZoom()
{
	alert("portrait");
	//document.getElementById("viewport").setAttribute('content','width = 320, initial-scale = 2.0, minimum-scale = 1.0, maximum-scale = 10.0');
}

function landscapeZoom()
{
	alert("landscape");
	//document.getElementById("viewport").setAttribute('content','width = 320, initial-scale = 2.0, minimum-scale = 1.0, maximum-scale = 10.0');
}
