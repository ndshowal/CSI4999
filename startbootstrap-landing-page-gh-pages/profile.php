<!DOCTYPE html>
<html lang="en">

  <?php
  	include("header.php");
  	$username = $_SESSION['username'];
  	$account_type = $_SESSION['account_type'];
  	
  	if(!$_SESSION['logged_in']) {
  		echo'<head><meta http-equiv="refresh" content="0;signin.php"></head>';
  	}
  	
  	if($account_type == 'Admin') {
  		echo'<head><meta http-equiv="refresh" content="0;admin.php"></head>';
  	} else {
  		echo'

		<body>
  		<!-- Masthead -->
    	<header class="masthead narrow text-white text-center">
	  	    <div class="overlay"></div>   
	    	<h1>Your Account Settings</h1>
    	</header>
	      
	  	<section class="standard-page profile">
			<div class="tab-pane active col-md-10 col-lg-8 col-xl-7 mx-auto" id="1">
				<!form id="edit-profile" class="form-horizontal">
					<fieldset>
						<div class="settings-section">
						
							<h4>Account Details</h4>
							
							<!-- USERNAME -->
								<div class="control-group">											
									<label class="control-label" for="username">Username &emsp; <a href="modify_account_settings.php?to_be_modified=username">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="username" value="'. $_SESSION['username'] .'" disabled>
									</div> <!-- /controls -->
								</div> <!-- /control-group -->
								
								<!-- FIRST NAME -->
								<div class="control-group">											
									<label class="control-label" for="firstname">First Name &emsp; <a href="modify_account_settings.php?to_be_modified=first_name">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="firstname" value="'. $_SESSION['first_name'] .'" disabled>
									</div> <!-- /controls -->
								</div> <!-- /control-group -->
								
								<!-- LAST NAME -->
								<div class="control-group">											
									<label class="control-label" for="lastname">Last Name &emsp; <a href="modify_account_settings.php?to_be_modified=last_name">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="lastname" value="'. $_SESSION['last_name'] .'" disabled>
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
								
								<!-- EMAIL -->
								<div class="control-group">											
									<label class="control-label" for="email">Email Address &emsp; <a href="modify_account_settings.php?to_be_modified=email_address">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="email" value="'. $_SESSION['email_address'] .'" disabled>
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
							</div>								
												
						<!-- CHANGE PASSWORD FORM -->
						<form method="post" action="process_password_change.php" id="password">
							<div class="settings-section">				
							
								<h4>Change Password</h4>
								
								<!-- CURRENT PASSWORD -->
								<div class="control-group">											
									<label class="control-label" for="password1">Enter Current Password</label>
									<div class="controls">
										<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
								
								<!-- NEW PASSWORD -->
								<div class="control-group">
									<label class="control-label" for="password1">New Password</label>
									<div class="controls">
										<input type="password" class="form-control form-control-lg" id="password1" name="new_password">
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
								
								<!-- CONFIRM NEW PASSWORD -->
								<div class="control-group">											
									<label class="control-label" for="password2">Confirm New Password</label>
									<div class="controls">
										<input type="password" class="form-control form-control-lg" id="password2" name="confirm_new_password">
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
								<div class="form-row">
	                				<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            					</div>
							</div>
						</form>
			
						<!-- DELETE ACCOUNT BUTTON -->
						<div class="form-actions">
							<button type="delete account" class="delete-account">Delete Account</button>
						</div>
						
						<!-- SAVE SETTINGS BUTTON -->
						<div class="form-actions">
							<button type="submit" class="btn btn-primary">Save Settings</button> 
							<button class="btn">Cancel</button>
						</div> <!-- /form-actions -->
						
					</fieldset>
				<!/form>
			</div>
		</section>';
  	}	
	include("footer.php");?>
	
  </body>
</html>