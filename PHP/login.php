<?PHP
$data = '';

$user = 'key';

if (!empty($_REQUEST) ) {
    if (!empty($_REQUEST[$user])) {
        if ($_REQUEST[$user] == 'testkey') {
            $data = '1|'.md5($_REQUEST[$user]).'|Successfully logged in!';
        }else{
            $data = '0|'.md5(date('Y-m-d h:i:s').rand(1, 9999)).'|Incorrect key // Get new key!';
        }
    }else{
        $data = '0|'.md5(date('Y-m-d h:i:s').rand(1, 9999)).'|Input your key first!';
    }
}

header('Content-Type: text/plain');
echo $data;
