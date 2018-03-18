<?php
    include("header.php");
    include("footer.php");
    
    $to_be_modified = $_GET['to_be_modified'];
    $error;
    
    echo'<div class="tab-pane active col-md-1 col-lg-8 col-xl-4 mx-auto" id="1">';
    
    switch($to_be_modified) {
        case 'username':
        	if($error == null) {
        		echo'<!-- CHANGE USERNAME FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="username">
					<div class="settings-section">				
					
						<h4>Change Username</h4>
						
						<!-- NEW USERNAME -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter New Username</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="password1" name="new_username">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            break;
        	} else {
            	echo'<!-- CHANGE USERNAME FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="username">
					<div class="settings-section">				
					
						<h4>Change Username</h4>
						
						<!-- NEW USERNAME -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter New Username</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="password1" name="new_username">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            	break;
        	}
        	
        case 'email_address':
        	if($error == null) {
        		echo'<!-- CHANGE EMAIL FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="email_address">
					<div class="settings-section">				
					
						<h4>Change Email Address</h4>
						
						<!-- NEW EMAIL -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter New Email Address</label>
							<div class="controls">
								<input type="email" class="form-control form-control-lg" id="password1" name="new_email_address">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- CURRENT PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
				break;
        	} else {
            	echo'<!-- CHANGE EMAIL FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="email_address">
					<div class="settings-section">				
					
						<h4>Change Email Address</h4>
						
						<!-- NEW EMAIL -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter New Email Address</label>
							<div class="controls">
								<input type="email" class="form-control form-control-lg" id="password1" name="new_email_address">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- CURRENT PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            	break;
        	}
        case 'first_name':
        	if($error == null) {
        		echo'<!-- CHANGE FIRST NAME FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="first_name">
					<div class="settings-section">				
					
						<h4>Change First Name</h4>
						
						<!-- NEW FIRST NAME -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter First Name</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="password1" name="new_first_name">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- CURRENT PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            	break;
        	} else {
        		echo'<!-- CHANGE FIRST NAME FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="first_name">
					<div class="settings-section">				
					
						<h4>Change First Name</h4>
						
						<!-- NEW FIRST NAME -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter First Name</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="password1" name="new_first_name">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- CURRENT PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            	break;
        	}
        case 'last_name':
        	if($error == null) {
        		echo'<!-- CHANGE LAST NAME FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="last_name">
					<div class="settings-section">				
					
						<h4>Change Last Name</h4>
						
						<!-- CURRENT PASSWORD -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter Last Name</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="password1" name="new_last_name">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- NEW PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            	break;
        	} else {
	            echo'<!-- CHANGE LAST NAME FORM -->
				<form method="post" action="process_profile_change.php?to_be_modified='.$to_be_modified.'" id="last_name">
					<div class="settings-section">				
					
						<h4>Change Last Name</h4>
						
						<!-- CURRENT PASSWORD -->
						<div class="control-group">											
							<label class="control-label" for="password1">Enter Last Name</label>
							<div class="controls">
								<input type="text" class="form-control form-control-lg" id="password1" name="new_last_name">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						
						<!-- NEW PASSWORD -->
						<div class="control-group">
							<label class="control-label" for="password1">Enter Password</label>
							<div class="controls">
								<input type="password" class="form-control form-control-lg" id="password1" name="entered_password">
							</div> <!-- /controls -->				
						</div> <!-- /control-group -->
						<!-- SUBMIT BUTTON -->
						<div class="form-row">
                			<button type="submit" align="middle" class="btn btn-lg btn-basic" name="submit_btn">Submit</button>
            			</div>
					</div>
				</form>';
            	break;
        	}
    }
    echo'</div>';
?>