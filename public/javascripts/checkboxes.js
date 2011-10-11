// Installing onclick handlers for some common libraries
checkboxesLoaded = false;

// First for jQuery
try {
	if(jQuery) {
		$(function () { // this line makes sure this code runs on page load
			$('.checkall').click(function () {
				$(this).parents('fieldset:eq(0)').find(':checkbox').attr('checked', this.checked);
			});
			checkboxesLoaded = true;
		});
	}
} catch (err) {}

//Then we try for dojo
try {
	if (!checkboxesLoaded && dojo) {
		dojo.addOnLoad(function(){
			dojo.query(".checkall").forEach(function(node, index, arr){
				dojo.connect(node, 'onclick', function() {
					dojo.query("input[type='checkbox']", node.parentNode).forEach(function(n, i, a) {
						dojo.attr(n , 'checked', node.checked);
					});
				});
			});
			checkboxesLoaded = true;
		});
	}
} catch (err) {}

//Then we default to standard behavior
try {
	// TODO
} catch (err) {}

