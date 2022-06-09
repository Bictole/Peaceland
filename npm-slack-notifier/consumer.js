const kafka = require('./kafka')
const discordHook = require('./discord_webhook')

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
      discordMessage += "Location : (" + alert.location.latitude.toString() + ", " + alert.location.longitude.toString() + ") :\n";
      discordMessage += "Words detected : " + alert.words.toString() + "\n";

      discordMessage += "Dangerous Persons : \n";
      for (p of alert.persons) {
        discordMessage += "\t=> " + p.name.toString() + " : " + p.peacescore.toString() + "\n";
      }
      
      discordMessage += "------------------------------------------------------------------------------------------------------------------";
      console.log(discordMessage);
      discordHook.send(discordMessage);

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
