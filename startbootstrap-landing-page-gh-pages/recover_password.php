<?php
    include("header.php");
    include("footer.php");
    
    echo'<!-- PASSWORD RECOVERY FORM -->
        <div class="tab-pane active col-md-1 col-lg-8 col-xl-4 mx-auto" id="1">
		    <form method="post" action="process_password_recovery.php" id="recovery_email">
			    <div class="settings-section">				
    			
				    <h4>Recover Password</h4>
    				
				    <!-- EMAIL ADDRESS  -->
				    <div class="control-group">											
					    <label class="control-label" for="email_address">Enter Email Address</label>
					    <div class="controls">
						    <input type="email" class="form-control form-control-lg" id="email_address" name="recovery_email">
    					</div> <!-- /controls -->				
				    </div> <!-- /control-group -->
				    <div class="form-row">
                    	<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            	    </div>
    			</div>
		    </form>
		</div>';
?>