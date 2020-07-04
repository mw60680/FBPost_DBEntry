<?php
define('FACEBOOK_SDK_V4_SRC_DIR', __DIR__.'/src/Facebook/');
require_once(__DIR__.'/src/Facebook/autoload.php');

$comments = $_GET['comments'];

$fbdata = array();
$fbdata['app_id'] = '443681039904891';
$fbdata['app_secret'] ='a0e379a4e5637cc92e3080be14d3bd0e';
$fbdata['default_graph_version'] = 'v5.0';

$fb = new Facebook\Facebook($fbdata);

$linkData = array();
$linkData['link'] = 'Page link';
$linkData['message'] = $comments;

$pageAccessToken='access token from FB';

try {
    $response = $fb->post('/me/feed', $linkData, $pageAccessToken);
} catch(Facebook\Exceptions\FacebookResponseException $e) {
    echo 'Graph returned an error: '.$e->getMessage();
    exit;
} catch(Facebook\Exceptions\FacebookSDKException $e) {
    echo 'Facebook SDK returned an error: '.$e->getMessage();
    exit;
}
$graphNode = $response->getGraphNode();
?>
