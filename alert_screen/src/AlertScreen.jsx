import React, {useState, useEffect} from 'react';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Logo from './Logo';
import Alert from '@mui/material/Alert';
import Divider from '@mui/material/Divider';

document.latestPersonID = 0; //yes this is hacky, idgaf

function addPeople2List(list, newList){
	let output = [...list];
	newList.map((person) =>{
		output.push({id: document.latestPersonID++, name: person.name, peacescore: person.peacescore})
	});
	console.log(output);
	return output;
}

function removeID(list, id){
	return list.filter((elem) => elem.id !== id);
}

function fetchNewAlerts(callback){
	fetch('http://localhost:4000/alert', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-type': 'application/json'
        }
    })
	  .then(res =>{ 
	    return res.json();
	  })
	  .then(data => {
	  	console.log("received: ");
	  	console.log(data);
	    callback(data);
	  })
}

function AlertScreen(props) {
	const [peopleList, setPeopleList] = useState([]);
	const [refresher, setRefresher] = useState(false);

	const getNewAlerts = () => {
		fetchNewAlerts(
			(newPeople) => setPeopleList(addPeople2List(peopleList, newPeople))
		);
	};

	useEffect(() => {
		const fetchAndWait = async () => {
			function sleep(ms) {
			  return new Promise(resolve => setTimeout(resolve, ms));
			}
			getNewAlerts();
			await sleep(1000);
			setRefresher(!refresher);
		};
		fetchAndWait();
	}, [refresher]);

	return (
		<Grid container style={{
			background: "linear-gradient(180deg, rgba(9,9,121,1) 0%, rgba(2,0,36,1) 25%)",
			minHeight: "100vh",
			color: "#FFF",
			textAlign: "center"
		}}>
		  <Grid item lg={12}>
			<Logo width="20vw" color="#FFF"/>
		  </Grid>
		  <Grid item lg={12} style={{minHeight: "15vh"}}>
			  <Typography variant="h2"> PeaceCorp<span style={{color: "yellow"}}>™</span> - The Eye </Typography>
		  </Grid>
		  <Grid item lg={12}>
		  	<Divider />
		  </Grid>
		  <Grid item lg={2}/>
		  <Grid item lg={8}>
		  	<Grid container direction={'column'} spacing={2}>
		  		{peopleList.map((person) => (
  			  		<Grid item lg={12} key={person.id}>
  					  	<Alert severity="warning" onClose={() => {setPeopleList(removeID(peopleList, person.id))}}>
  					  		<Typography variant="subtitle1" align="center">
  					  			<strong>[Peaceless individual detected]</strong>  -- The citizen <strong>{person.name}</strong> has an worrying peacescore of <strong>{person.peacescore}.</strong>. 
  					  		</Typography>
  					  	</Alert>
  				  	</Grid>
		  		))}
		  	</Grid>
		  </Grid>
		</Grid>
	);
}

export default AlertScreen;