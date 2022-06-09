const { Webhook } = require('discord-webhook-node');
const hook = new Webhook("https://discord.com/api/webhooks/984421786911387668/0sH65bvcnmDKelMd2tSx_PbSIOvghTYvUWru5jVcI3L53OpRG94iivR9ULDgAJDKmClv");
 
const IMAGE_URL = 'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/fd1994f4-a0b8-4c12-a88d-0814edbfe711/d4wkidp-f83a637d-ea16-46a5-85ad-508af97b4470.jpg/v1/fill/w_1131,h_707,q_75,strp/starcraft_2_probe_by_worthart-d4wkidp.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwic3ViIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl0sIm9iaiI6W1t7InBhdGgiOiIvZi9mZDE5OTRmNC1hMGI4LTRjMTItYTg4ZC0wODE0ZWRiZmU3MTEvZDR3a2lkcC1mODNhNjM3ZC1lYTE2LTQ2YTUtODVhZC01MDhhZjk3YjQ0NzAuanBnIiwid2lkdGgiOiI8PTExMzEiLCJoZWlnaHQiOiI8PTcwNyJ9XV19.-WjoyTunzw5TOC-2bHphzRjZuREzSRO71OF1UcMHt8U';
hook.setAvatar(IMAGE_URL);
hook.setUsername('Discord Webhook Node Name');
 
//hook.send("Hello there!");

module.exports = hook
