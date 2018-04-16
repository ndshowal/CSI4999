<?php
    include("header.php");
    
    $txusername = $_SESSION['username'];
    $tx_ID = $_GET['id'];
    
    $query = "select *
            FROM transactions
            WHERE transaction_ID='$tx_ID'";
            
    $response = @mysqli_query($db, $query);

    if($response) {
        while($row = mysqli_fetch_array($response)) {
            $txStatus;
            if($row['in_progress'] == 1) {
                $txStatus = "In progress";
            } else {
                $txStatus = "Completed";
            }
            
            $txAccepted;
            if($row['confirmed'] == 1) {
                $txAccepted = "Accepted";
            } else {
                $txAccepted = "Denied";
            }
            
    echo'
    <header class="masthead text-white text-center">
      <div class="overlay"></div>
      <div class="container">
        <h1>Transaction Details</h1>
      </div>
    </header>
    
    <section class="standard-page transaction">
    <div class="container">
    <div class="row"><div class="col-md-10 col-lg-8 col-xl-7 mx-auto">';
            echo'
            
                <p><b>Transaction Initiated by:</b> ' . $row['initiator'] . ' </p>
                <br><p><b>Sender:</b> ' . $row['sender'] . '</p>
                <p><b>Recipient:</b> ' . $row['recipient'] . ' </p>
                <p><b>Amount of Transaction:</b> $' . $row['transaction_amount'] . ' </p>
                <p><b>Memo:</b> ' . $row['memo'] . ' </p>
                <p><b>Transaction was initiated on:</b> ' . $row['start_date'] . ' <p>';
                if($row['completion_date'] != null) {
                    echo'<p><b>Transaction was completed on:</b> ' . $row['completion_date'] . ' </p>';
                    if($row['sender'] === $row['initiator']) {
                        echo'<b>Transaction was ' . $txAccepted . ' by ' . $row['recipient'] . '</b></p>';
                    } else if($row['recipient'] === $row['initiator']) {
                        echo'<b>Transaction was ' . $txAccepted . ' by ' . $row['sender'] . '</b></p>';
                    }
                } else {
                    if($row['sender'] === $row['initiator']){
                        echo'<br><b>Transaction not yet approved by '. $row['recipient'] . '</b></p>';
                    } else {
                        echo'<br><b>Transaction not yet approved by '. $row['sender'] . '<.b></p>';
                    }
                }
                echo'<a href="transactions.php" class="btn btn-primary">Back</a>';
            }
    }
    
    echo '</div></div></section>';
    include("footer.php");

?>