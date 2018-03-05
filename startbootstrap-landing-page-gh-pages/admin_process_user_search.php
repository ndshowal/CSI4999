  <?php
  	include("header.php");
  	include("footer.php");
    $account_type = $_SESSION['account_type'];
  	
  	if(!$_SESSION['logged_in']) {
  		echo'<head><meta http-equiv="refresh" content="0;signin.php"></head>';
  	}
  	
  	if($account_type != 'Admin') {
  		echo'<head><meta http-equiv="refresh" content="0;profile.php"></head>';
  	} else {
  	    
  	    $username_search = $_POST['username_search'];
        
        $query = "SELECT * FROM users
                WHERE username='$username_search'";
    
        $response = @mysqli_query($db, $query);
                
        if ($response) {
            while($row = mysqli_fetch_array($response)) {
                if($row['username'] == $username_search) {
                    $user_exists = true;
                    $searched_username = $username_search;
                    $searched_password = $row['password'];
                    $searched_first_name = $row['first_name'];
                    $searched_last_name = $row['last_name'];
                    $searched_email_address = $row['email_address'];
                    $searched_account_type = $row['account_type'];
                    
                    $_SESSION['searched_username'] = $searched_username;
                    $_SESSION['searched_password'] = $searched_password;
                    $_SESSION['searched_first_name'] = $searched_first_name;
                    $_SESSION['searched_last_name'] = $searched_last_name;
                    $_SESSION['searched_email_address'] = $searched_email_address;
                    $_SESSION['searched_account_type'] = $searched_account_type;
                    $_SESSION['admin_change'] = true;
                } 
            }
            if(!$user_exists) {
                echo 'User not found, please try again.';
                echo'<head><meta http-equiv="refresh" content="2;profile.php"></head>';
                exit();
            }
        } 
  	
  	    echo'
		<body>
  		<!-- Masthead -->
    	<header class="masthead narrow text-white text-center">
	  	    <div class="overlay"></div>   
	    	<h1>Modify User Account Details</h1>
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
									<input type="text" class="form-control form-control-lg" id="username" value="'. $searched_username .'" disabled>
								</div> <!-- /controls -->
							</div> <!-- /control-group -->
								
								<!-- FIRST NAME -->
								<div class="control-group">											
									<label class="control-label" for="firstname">First Name &emsp; <a href="modify_account_settings.php?to_be_modified=first_name">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="firstname" value="'. $searched_first_name .'" disabled>
									</div> <!-- /controls -->
								</div> <!-- /control-group -->
								
								<!-- LAST NAME -->
								<div class="control-group">											
									<label class="control-label" for="lastname">Last Name &emsp; <a href="modify_account_settings.php?to_be_modified=last_name">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="lastname" value="'. $searched_last_name .'" disabled>
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
								
								<!-- EMAIL -->
								<div class="control-group">											
									<label class="control-label" for="email">Email Address &emsp; <a href="modify_account_settings.php?to_be_modified=email_address">Edit</a></label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="email" value="'. $searched_email_address .'" disabled>
									</div> <!-- /controls -->				
								</div> <!-- /control-group -->
							</div>								
												
						<!-- CHANGE PASSWORD FORM -->
						<form method="post" action="process_password_change.php" id="password">
							<div class="settings-section">				
							
								<h4>Change Password</h4>
								
								<!-- CURRENT PASSWORD -->
								<div class="control-group">											
									<label class="control-label" for="password1">Current Password</label>
									<div class="controls">
										<input type="text" class="form-control form-control-lg" id="password1" value="'.$searched_password.'" name="entered_password">
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
  	?>