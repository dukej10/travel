db.createUser({
  user: "root",
  pwd: "toor",
  roles: [
    {
      role: "readWrite",
      db: "testDB",
    },
  ],
});
db.createCollection("app_users", { capped: false });

db.app_users.insert([
  {
    username: "ragnar777",
    dni: "VIKI771012HMCRG093",
    enabled: true,
    password: "$2a$10$guY6xP201iZ.16pdhuEdRuUPibqJ3R.hkO14jXOVK6o/N.6Dqq8De",
    role: {
      granted_authorities: ["ROLE_USER"],
    },
  },
  {
    username: "heisenberg",
    dni: "BBMB771012HMCRR022",
    enabled: true,
    password: "$2a$10$VTJksn0sZGyLVPx2o6EfJupZfT0LNgle7hXSrkZBzFF8buLqVH4QW",
    role: {
      granted_authorities: ["ROLE_USER"],
    },
  },
  {
    username: "misterX",
    dni: "GOTW771012HMRGR087",
    enabled: true,
    password: "$2a$10$xvBa9IZwpCNR/QrMda27jewQmZj6WKaNEqqP/Rlk8OPN53qrugiSW",
    role: {
      granted_authorities: ["ROLE_USER", "ROLE_ADMIN"],
    },
  },
  {
    username: "neverMore",
    dni: "WALA771012HCRGR054",
    enabled: true,
    password: "$2a$10$nWd1Rrq1dBnnR1Kn0MUlZemt6q8LO5KhJ55iZAcfgSYLk8D6CkAzK",
    role: {
      granted_authorities: ["ROLE_ADMIN"],
    },
  },
]);
