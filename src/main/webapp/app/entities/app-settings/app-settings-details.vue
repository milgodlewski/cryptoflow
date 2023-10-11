<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="appSettings">
        <h2 class="jh-entity-heading" data-cy="appSettingsDetailsHeading"><span>App Settings</span> {{ appSettings.id }}</h2>
        <dl class="row jh-entity-details"></dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="appSettings.id"
          :to="{ name: 'AppSettingsEdit', params: { appSettingsId: appSettings.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./app-settings-details.component.ts"></script>
