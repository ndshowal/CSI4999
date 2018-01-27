<!DOCTYPE html>
<html lang="en">

  <?php
  	include("header.php");
  ?>

  <body>
			<div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
			<h2 class="page-title">
			<i class="icon-th-large"></i>				
			</h1>
				<div class="row">
					<div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
						<div class="widget">
							<div class="widget-content">
								<div class="tabbable">

							<div class="tab-content">

								<div class="tab-pane active" id="1">

								<form id="edit-profile" class="form-horizontal">

									<fieldset>

										

										<div class="control-group">											
											<h3>Your Account Details</h3>
											<label class="control-label" for="username">Username</label>

											<div class="controls">

												<input type="text" class="input-medium disabled" id="username" value="user" disabled>

											</div> <!-- /controls -->				

										</div> <!-- /control-group -->

										

										

										<div class="control-group">											

											<label class="control-label" for="firstname">First Name</label>

											<div class="controls">

												<input type="text" class="input-medium" id="firstname" value="Rod" disabled>
 
											</div> <!-- /controls -->				

										</div> <!-- /control-group -->

										

										

										<div class="control-group">											

											<label class="control-label" for="lastname">Last Name</label>

											<div class="controls">

												<input type="text" class="input-medium" id="lastname" value="Lee" disabled>

											</div> <!-- /controls -->				

										</div> <!-- /control-group -->

										

										

										<div class="control-group">											

											<label class="control-label" for="email">Email Address</label>

											<div class="controls">

												<input type="text" class="input-large" id="email" value="example@example.com" disabled>

											</div> <!-- /controls -->				

										</div> <!-- /control-group -->

										

										

										<br /><br />

										

										<div class="control-group">											
											<h4>Change Password</h4>
											
											<label class="control-label" for="password1">Enter Current Password</label>

											<div class="controls">

												<input type="password" class="input-medium" id="password1" value="password">

											</div> <!-- /controls -->				

										</div> <!-- /control-group -->
											
											<label class="control-label" for="password1">New Password</label>

											<div class="controls">

												<input type="password" class="input-medium" id="password1" value="password">

											</div> <!-- /controls -->				

										</div> <!-- /control-group -->

										

										

										<div class="control-group">											

											<label class="control-label" for="password2">Confirm New Password</label>

											<div class="controls">

												<input type="password" class="input-medium" id="password2" value="password">

											</div> <!-- /controls -->				

										</div> <!-- /control-group -->
										<div class="form-actions">
										<button type="delete account" class="btn btn-primary">Delete</button>
										</div>

										

										

											

											<br />

										

											

										<div class="form-actions">

											<button type="submit" class="btn btn-primary">Save</button> 

											<button class="btn">Cancel</button>

										</div> <!-- /form-actions -->

									</fieldset>

								</form>

								</div>


</div> <!-- /widget-content -->
</div> <!-- /widget -->
</div> <!-- /co-md-10 col-lg-8 col-xl-7 mx-auto -->
</div> <!-- /row -->


</div> <!-- /col-md-10 col-lg-8 col-xl-7 mx-auto-->
</div> <!-- /row -->
</div> <!-- /container -->
</div> <!-- /content -->

					

	

 <?php
	include("footer.php");
?>



  </body>

</html>