const kafka = require('./kafka')
const discordHook = require('./discord_webhook')
const axios = require('axios')

const consumer = kafka.consumer({
  groupId: "alert"
})

const main = async () => {
  await consumer.connect()

  await consumer.subscribe({
    topic: "alert",
    fromBeginning: true
  })

  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      console.log('Received message', {
        topic,
        partition,
        key: message.key.toString(),
        value: message.value.toString()
      })
      let alert = JSON.parse(message.value);
      let discordMessage = "[ALERT] - " + alert.timestamp.toString().split(".")[0] + " - PeaceWatcher_" + alert.peacewatcher_id.toString() + "\n";
      discordMessage += "Location : (" + alert.location.latitude.toFixed(6).toString() + ", " + alert.location.longitude.toFixed(6).toString() + ")\n";
      discordMessage += "Words detected : | "; 
      for (w of alert.words) {
        discordMessage += "\"" + w + "\"" + " | ";
      }
      discordMessage += "\n";

      discordMessage += "Dangerous Persons : \n";
      for (p of alert.persons) {
        discordMessage += "\t=> " + p.name.toString() + " : " + p.peacescore.toFixed(2).toString() + "\n";
      }
      
      discordMessage += "------------------------------------------------------------------------------------------------------------------";
      console.log(discordMessage);
      discordHook.send(discordMessage);


        axios.post('http://localhost:4000/alert', alert.persons)
        .then(function (response) {
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
        });
     
  
    }
  })
}

main().catch(async error => {
  console.error(error)
  try {
    await consumer.disconnect()
  } catch (e) {
    console.error('Failed to gracefully disconnect consumer', e)
  }
  process.exit(1)
})
