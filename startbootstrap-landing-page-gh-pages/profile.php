<!DOCTYPE html>
<html lang="en">

  <?php
  	include("header.php");
	echo'

	<body>
  	<!-- Masthead -->
      <header class="masthead narrow text-white text-center">
  	    <div class="overlay"></div>   
	      <h1>Your Account Settings</h1>
      </header>
      
  	<section class="standard-page profile">
			
		<div class="tab-pane active col-md-10 col-lg-8 col-xl-7 mx-auto" id="1">
			
			<form id="edit-profile" class="form-horizontal">
				<fieldset>
				<div class="settings-section">
					<h4>Account Details</h4>
					<div class="control-group">											
						<label class="control-label" for="username">Username</label>
						<div class="controls">
							<input type="text" class="form-control form-control-lg" id="username" value="user" disabled>
						</div> <!-- /controls -->				
					</div> <!-- /control-group -->
					<div class="control-group">											
						<label class="control-label" for="firstname">First Name</label>
						<div class="controls">
								<input type="text" class="form-control form-control-lg" id="firstname" value="Rod" disabled>
	 					</div> <!-- /controls -->				
					</div> <!-- /control-group -->
					<div class="control-group">											
						<label class="control-label" for="lastname">Last Name</label>
						<div class="controls">
							<input type="text" class="form-control form-control-lg" id="lastname" value="Lee" disabled>
						</div> <!-- /controls -->				
					</div> <!-- /control-group -->
					<div class="control-group">											
						<label class="control-label" for="email">Email Address</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="email" value="example@example.com" disabled>
							</div> <!-- /controls -->				
					</div> <!-- /control-group -->
			</div>								
											
			<div class="settings-section">								
					<h4>Change Password</h4>
						<div class="control-group">											
							<label class="control-label" for="password1">Enter Current Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						<div class="control-group">
								<label class="control-label" for="password1">New Password</label>
								<div class="controls">
									<input type="password" class="form-control form-control-lg" id="password1">
								</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						<div class="control-group">											
							<label class="control-label" for="password2">Confirm New Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password2">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						</div>
						
						<div class="form-actions">
							<button type="delete account" class="delete-account">Delete Account</button>
						</div>

				
						<div class="form-actions">
							<button type="submit" class="btn btn-primary">Save Settings</button> 
							<button class="btn">Cancel</button>
						</div> <!-- /form-actions -->
					
					</fieldset>
			</form>
		</div>
	</section>';
	include("footer.php");?>
  </body>
</html>