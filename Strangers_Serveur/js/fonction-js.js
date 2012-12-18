/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//fonction pour tester les numéro de tel !
function Verifier_Numero_Telephone(num_tel)
{
	// Definition du motif a matcher
	var regex = new RegExp(/^(01|02|03|04|05|06|08)[0-9]{8}/gi);
	
	// Definition de la variable booleene match
	var match = false;
	
	// Test sur le motif
	if(regex.test(num_tel))
	{
		match = true;
	}
	  else
	{
		match = false;
	}
	
	// On renvoie match
	return match;
}
