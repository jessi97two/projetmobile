<?php

/*
Dans ce fichier, on définit diverses fonctions permettant de récupérer des données utiles pour notre projet. 
*/


// inclure ici la librairie faciliant les requêtes SQL
include_once("maLibSQL.pdo.php");

// cette fonction crée un utilisateur 
function ajouterUtilisateur($nom,$prenom,$login,$passe,$mail,$tel)
{
	$SQL = "INSERT INTO users(nom,prenom,login,password,mail,tel) VALUES ('$nom','$prenom','$login','$passe','$mail','$tel')";
	return SQLInsert($SQL);
}

// cette fonction recherche si l'utilisateur voulant créer un compte existe déja
function rechercheUtilisateur($nom,$prenom,$mail,$tel) 
{
	$SQL = "SELECT id FROM users WHERE nom='$nom' AND prenom='$prenom' AND mail='$mail' AND tel='$tel'";
	return parcoursRs(SQLSelect($SQL));
}

// cette fonction récupère l'id de l'utilisateur qui souhaite se connecté
function getUser($login,$passe) {
	$SQL = "SELECT id,nom,prenom FROM users WHERE login='$login' AND password='$passe'";
	return parcoursRs(SQLSelect($SQL));
}

// cette fonction change le boolean et passe l'user en mode connecté
function connectUser($id) {
	$SQL = "UPDATE users SET connecte = 1 WHERE id='$id'";
	SQLUpdate($SQL);
}

function rechercheUser($recherche) {
	$SQL = "SELECT nom,prenom,login FROM users WHERE nom LIKe '%$recherche%' OR prenom LIKE '%$recherche%' OR login LIKE '%$recherche%'";
	return parcoursRs(SQLSelect($SQL));
}

function addFollower($iduser,$idfollower) {
	$SQL = "INSERT INTO followers(iduser,idfollower) VALUES('$iduser','$idfollower')";
	return SQLInsert($SQL);
}

function getidFollower($nom,$prenom,$login) {
	$SQL = "SELECT id FROM users WHERE nom='$nom' AND prenom='$prenom' AND login='$login'";
	return parcoursRs(SQLSelect($SQL));
}

function getFollowers($iduser) {
	$SQL = "SELECT u.nom,u.prenom,u.login FROM followers AS f , users AS u WHERE f.iduser = '$iduser' AND u.id = f.idfollower";
	return parcoursRs(SQLSelect($SQL));
}

function getPools($iduser) {
	$SQL = "SELECT s.titre, s.id FROM sondages AS s
	LEFT JOIN reponsessondages AS r ON r.idsondage = s.id
	LEFT JOIN groupes AS g ON g.id = s.idgroupe
	LEFT JOIN liens_users_groupes AS l ON l.idgroupe = s.idgroupe
	WHERE  s.iduser != '$iduser' AND 0 = (SELECT count(*) FROM reponsessondages WHERE idsondage=s.id AND iduser = l.iduser)
	GROUP BY s.titre";
	return parcoursRs(SQLSelect($SQL));
}

function getInvitations($iduser) {
	$SQL = "SELECT i.id, e.nom FROM events AS e 
			LEFT JOIN invitations AS i ON i.idevent = e.id 
			LEFT JOIN groupes AS g ON g.id = i.idgroupe 
			LEFT JOIN liens_users_groupes AS l ON l.idgroupe = g.id
			LEFT JOIN participationsevents AS p ON p.idevent = i.idevent 
			WHERE l.iduser = '$iduser' AND 0=(SELECT count(*) FROM participationsevents WHERE iduser='$iduser' AND idevent=i.idevent)";
	return parcoursRs(SQLSelect($SQL));
}

function getEventsProche($iduser) {
	$SQL = "SELECT e.nom FROM events AS e
			LEFT JOIN participationsevents AS p ON p.idevent = e.id
			WHERE DATEDIFF(e.dateEvent , NOW()) < 7 AND DATEDIFF(e.dateEvent , NOW()) > 0 AND p.iduser = '$iduser'";
	return parcoursRs(SQLSelect($SQL));
}

function getInfosSondage($idsondage) {
	$SQL = "SELECT s.titre, s.descriptif, u.login as sondeur FROM sondages AS s, users AS u 
	WHERE s.id = '$idsondage' AND s.iduser = u.id";
	return parcoursRs(SQLSelect($SQL));
}

function getPropositionsDatesSondage($idsondage) {
	$SQL = "SELECT p.datepropose, p.id FROM propositionsdate AS p
	LEFT JOIN sondages as s ON p.idsondage = s.id
	WHERE s.id = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}


function getPropositionsRestaurantSondage($idsondage) {
	$SQL = "SELECT r.nom, r.id FROM propositionsrestaurant AS p
	LEFT JOIN sondages as s ON p.idsondage = s.id
	LEFT JOIN restaurants AS r ON r.id = p.idrestaurant
	WHERE s.id = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}

function addReponseSondage($iduser,$idsondage,$iddate,$idresto) {
	$SQL = "INSERT INTO reponsessondages(iduser,idsondage,iddate,idrestaurant) VALUES('$iduser','$idsondage','$iddate','$idresto')";
	return SQLInsert($SQL);
}

function getInfosInvitation($idinvitation) {
	$SQL = "SELECT e.id AS idevent, e.nom AS nomevent, e.dateEvent, e.heureDebut, r.nom AS restaurant, u.nom, u.prenom FROM invitations AS i
			LEFT JOIN events AS e ON e.id = i.idevent
			LEFT JOIN restaurants AS r ON r.id = e.idrestaurant
			LEFT JOIN users AS u ON u.id = e.iduser
			WHERE i.id = '$idinvitation'";
	return parcoursRs(SQLSelect($SQL));
}

function putReponseInvitation($iduser,$idevent,$reponse) {
	$SQL = "INSERT INTO participationsevents(iduser,idevent,reponse) VALUES('$iduser','$idevent','$reponse')";
	return SQLInsert($SQL);
}

function getGroups($iduser) {
	$SQL = "SELECT id,nom,idOnTel FROM groupes WHERE iduser = '$iduser' ";
	return parcoursRs(SQLSelect($SQL));
}

function addGroup($iduser,$group,$groupidphone) {
	$SQL = "INSERT INTO groupes(nom,iduser,idOnTel) VALUES('$group','$iduser','$groupidphone')";
	return SQLInsert($SQL);
}

function removeGroup($iduser,$group) {
	$SQL = "DELETE FROM groupes WHERE iduser = '$iduser' AND nom = '$group'";
	return SQLDelete($SQL);
}

function getRestaurants($iduser) {
	$SQL = "SELECT id,nom FROM restaurants WHERE iduser = '$iduser'";
	return parcoursRs(SQLSelect($SQL));
	
}

function addSondage($iduser,$titre,$description,$groupe) {
	$SQL = "INSERT INTO sondages(iduser,idgroupe,titre,descriptif) VALUES('$iduser','$groupe','$titre','$description')";
	return SQLInsert($SQL);
}

function getGroupById($idgroupe) {
	$SQL = "SELECT nom FROM groupes WHERE id = '$idgroupe' ";
	return parcoursRs(SQLSelect($SQL));
}

function getRestaurantById($idrestaurant) {
	$SQL = "SELECT nom FROM restaurants WHERE id = '$idrestaurant'";
	return parcoursRs(SQLSelect($SQL));
}

function addPropositionRestaurant($idsondage,$idrestaurant) {
	$SQL = "INSERT INTO propositionsrestaurant(idsondage,idrestaurant) VALUES('$idsondage','$idrestaurant')";
	return SQLInsert($SQL);
}

function addPropositionDate($idsondage,$datepropose) {
	$SQL = "INSERT INTO propositionsdate(idsondage,datepropose) VALUES('$idsondage','$datepropose')";
	return SQLInsert($SQL);
}

function getContactByNumber($numero) {
	$SQL = "SELECT id FROM users WHERE tel = '$numero'";
	return parcoursRs(SQLSelect($SQL));
}

function getLienContactGroup($idcontact,$groupe) {
	$SQL = "SELECT id FROM liens_users_groupes WHERE iduser = '$idcontact' AND idgroupe = '$groupe'";
	return parcoursRs(SQLSelect($SQL));
}

function addLienContactGroupe($idcontact,$groupe) {
	$SQL = "INSERT INTO liens_users_groupes(iduser,idgroupe) VALUES('$idcontact','$groupe')";
	return SQLInsert($SQL);
}

function getInOnTelGroup($idgroupe) {
	$SQL = "SELECT idOnTel FROM groupes WHERE id = '$idgroupe'";
	return parcoursRs(SQLSelect($SQL));
}

function getAllSondagesUser($iduser) {
	$SQL = "SELECT id,titre FROM sondages WHERE iduser = '$iduser' AND isArchived = 0";
	return parcoursRs(SQLSelect($SQL));
}

function getResultatsDateSondage($idsondage) {
	$SQL = "SELECT count(*) AS nbre, d.datepropose as choixdate, d.id AS iddate FROM reponsessondages AS r
			JOIN propositionsdate as d on d.id = r.iddate
			WHERE r.idsondage = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}

function getUsersResultatsDateSondage($idsondage,$iddate) {
	$SQL = "SELECT u.login FROM users AS u 
			JOIN reponsessondages AS r ON r.iduser = u.id
			WHERE r.idsondage = '$idsondage' AND r.iddate = '$iddate'";
	return parcoursRs(SQLSelect($SQL));
}

function getResultatsRestaurantSondage($idsondage) {
	$SQL = "SELECT count(*) AS nbre, r.nom AS choixresto, r.id AS idrestaurant FROM reponsessondages AS rs
			JOIN propositionsrestaurant as p on p.id = rs.idrestaurant
            JOIN restaurants as r on p.idrestaurant = r.id
			WHERE rs.idsondage = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}

function getUsersResultatsRestaurantSondage($idsondage,$idrestaurant) {
	$SQL = "SELECT u.login FROM users AS u 
			JOIN reponsessondages AS r ON r.iduser = u.id
			WHERE r.idsondage = '$idsondage' AND r.idrestaurant = '$idrestaurant'";
	return parcoursRs(SQLSelect($SQL));
}

function getGroupByIdSondage($idsondage) {
	$SQL = "SELECT g.nom, g.id FROM groupes AS g 
			JOIN sondages AS s ON s.idgroupe = g.id
			WHERE s.id = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}

function getRestaurantBySondageName($idsondage,$nomrestaurant) {
	$SQL ="SELECT r.id FROM restaurants AS r JOIN propositionsrestaurant AS s ON s.idrestaurant = r.id WHERE s.idsondage = '$idsondage' AND r.nom = '$nomrestaurant'";
	return parcoursRs(SQLSelect($SQL));
}

function addEvent($titre,$dateevent,$heure,$restaurant,$iduser,$idgroupe) {
	$SQL = "INSERT INTO events(nom,dateEvent,heureDebut,idrestaurant,iduser,idgroupe) VALUES('$titre','$dateevent','$heure','$restaurant','$iduser','$idgroupe')";
	return SQLInsert($SQL);
}

function archiverSondage($idsondage) {
	$SQL = "UPDATE sondages SET isArchived = 1 WHERE id='$idsondage'";
	SQLUpdate($SQL);
}

function getAllEventsUser($iduser) {
	$SQL = "SELECT id,nom FROM events WHERE iduser = '$iduser' AND isArchived = 0";
	return parcoursRs(SQLSelect($SQL));
}

function getInfosEvent($idevent) {
	$SQL = "SELECT e.nom AS nomEvent,e.dateEvent,e.heureDebut,r.nom AS restaurant FROM events AS e 
			JOIN restaurants AS r ON r.id = e.idrestaurant 
			WHERE e.id = '$idevent'";
	return parcoursRs(SQLSelect($SQL));
}

function getReponsesUsersEvent($idevent) {
	$SQL = "SELECT u.login, p.reponse FROM events AS e  
			JOIN participationsevents AS p ON p.idevent = e.id
			JOIN users AS u ON u.id = p.iduser
			WHERE e.id = '$idevent'";
	return parcoursRs(SQLSelect($SQL));
}

function removeLiensGroupContacts($idgroupe) {
	
}

/*

SELECT s.titre, s.descriptif, u.login as sondeur, p.datepropose FROM sondages AS s
	LEFT JOIN users AS u ON s.iduser = u.id
	LEFT JOIN propositionsdate as p ON p.idsondage = s.id
	WHERE s.id = 1
*/
?>