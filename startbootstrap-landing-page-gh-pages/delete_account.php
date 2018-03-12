<html>
    <body>
    <?php
        include("header.php");
        include("footer.php");
    ?>
        
    <section class="standard-page profile">
	    <div class="tab-pane active col-md-10 col-lg-8 col-xl-7 mx-auto" id="1">
            <form method="post" action="process_account_deletion.php" id="confirm" onsubmit="return confirm('Are you sure you want to delete your account?\nTHIS CANNOT BE REVERSED!');">
		        <div class="settings-section">				
    				
	    		<h4>Delete Account</h4>
			        
    			<!-- CURRENT PASSWORD -->
			    <div class="control-group">											
    				<label class="control-label" for="password1">Enter Password</label>
				    <div class="controls">
    					<input type="password" class="form-control form-control-lg" id="password1" name="password">
				    </div> <!-- /controls -->				
			    </div> <!-- /control-group -->
    			
			    <!-- CONFIRM PASSWORD -->
			    <div class="control-group">											
    				<label class="control-label" for="password2">Confirm Password</label>
    			    <div class="controls">
    	  					<input type="password" class="form-control form-control-lg" id="password2" name="confirm_password">
		  			    </div> <!-- /controls -->				
			    </div> <!-- /control-group -->
						    
			    <!-- SUBMIT BUTTON -->
        	    <div class="control-group">
    	            <button type="submit" align="middle" class="btn btn-lg btn-basic" name="delete_btn">Delete</button>
        	    </div>
            </div>
		</div>
    </section>

    
    </body>
</html>