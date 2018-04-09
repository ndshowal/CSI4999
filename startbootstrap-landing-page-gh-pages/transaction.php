<?php
    include("header.php");
    include("footer.php");
    
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
            
            echo'<h3> Transaction' . $tx_ID . '</h3><br>
                Sender: ' . $row['sender'] . ' <br>
                Recipient: ' . $row['recipient'] . ' <br>
                Transaction Initiated by: ' . $row['initiator'] . ' <br>
                Amount of Transaction: $' . $row['transaction_amount'] . ' <br>
                Memo: ' . $row['memo'] . ' <br>
                Transaction was initiated on: ' . $row['start_date'] . ' <br>';
                if($row['completion_date'] != null) {
                    echo'Transaction was completed on: ' . $row['completion_date'] . ' <br>
                    Transaction was ' . $txAccepted . '.';
                } else {
                    echo'Transaction is still in progress.';
                }
                echo'<br><br><a href="transactions.php">Back</a>';
            }
    }
?>